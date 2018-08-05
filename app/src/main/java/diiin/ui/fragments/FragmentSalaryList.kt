package diiin.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Salary
import diiin.model.SalaryT
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertSalaryActivity
import diiin.ui.activity.MainActivity
import diiin.ui.adapter.SalaryListAdapter
import diiin.util.MathService
import diiin.util.MessageDialog
import diiin.util.SalarySharedPreferences
import java.util.*

/**
 * Screen that shows to user the incomes filter and salary list
 *
 * @author Gabriel Moro
 */
class FragmentSalaryList : Fragment(), MainActivity.MainPageFragments {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
            var mbtnInsertSalary: FloatingActionButton? = null
    private var mithItemHelperReference : ItemTouchHelper? = null

    companion object {
        const val NAME = "FragmentSalaryList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_salarylist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view?.findViewById(R.id.spMonthSelector)
        mrvSalaryList = view?.findViewById(R.id.rvSalaryList)
        mbtnInsertSalary = view?.findViewById(R.id.btnaddSalary)
        mbtnInsertSalary?.setOnClickListener {
            context.startActivity(Intent(context, InsertSalaryActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvSalaryList?.layoutManager = llManager
        if(mbtnInsertSalary!=null)
            mrvSalaryList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtnInsertSalary!!))
    }

    override fun onResume() {
        super.onResume()
        loadPageContent()
    }

    private fun loadTouchHelperListener(aslSalaryAdapter : SalaryListAdapter) {

        if(mithItemHelperReference != null) {
            mithItemHelperReference!!.attachToRecyclerView(null)
            mithItemHelperReference = null
        }

        mithItemHelperReference = ItemTouchHelper(SalaryListTouchHelper(context, aslSalaryAdapter))
        mithItemHelperReference!!.attachToRecyclerView(mrvSalaryList)
    }

    override fun loadPageContent() {
        val lstSalary = StaticCollections.mappDataBuilder?.salaryDao()?.all() ?: return
        mrvSalaryList ?: return

        val lstSalaryFiltered : ArrayList<SalaryT> = ArrayList<SalaryT>()
        val slAdapter : SalaryListAdapter

        if(StaticCollections.mmtMonthSelected == null) {
            lstSalaryFiltered.addAll(lstSalary)
            slAdapter = SalaryListAdapter(lstSalaryFiltered, context)
        } else {
            lstSalary.forEach{
                val clCalendar = Calendar.getInstance()
                clCalendar.time = MathService.stringToCalendarTime(it.mstrDate)
                if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR) == StaticCollections.mnYearSelected)
                    lstSalaryFiltered.add(it)
            }
            slAdapter = SalaryListAdapter(lstSalaryFiltered, context)
        }

        mrvSalaryList?.adapter = slAdapter
        loadTouchHelperListener(slAdapter)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvSalaryList?.adapter?.notifyDataSetChanged()
    }

}

class SalaryListTouchHelper(actxContext : Context, aeaSalaryAdapter : SalaryListAdapter) : ItemTouchHelper.Callback() {

    private val meaSalaryAdapter : SalaryListAdapter = aeaSalaryAdapter
    private val mctxContext : Context = actxContext

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val nDragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val nSwipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(nDragFlags, nSwipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDismiss(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return StaticCollections.mbEditMode
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return StaticCollections.mbEditMode
    }

    private fun onItemMove(nFromPosition : Int, nToPosition : Int) : Boolean {
        if(nFromPosition < nToPosition) {
            var nCount = nFromPosition
            while(nCount < nToPosition) {
                Collections.swap(meaSalaryAdapter.mltSalaryList, nCount, nCount+1)
                nCount++
            }
        } else {
            var nCount = nFromPosition
            while (nCount > nToPosition) {
                Collections.swap(meaSalaryAdapter.mltSalaryList, nCount, nCount-1)
                nCount--
            }
        }
        meaSalaryAdapter.notifyItemMoved(nFromPosition, nToPosition)
        return true
    }

    private fun onItemDismiss(nPosition : Int) {
        val salaryTarget = meaSalaryAdapter.mltSalaryList[nPosition]

        MessageDialog.showMessageDialog(mctxContext,
                mctxContext.resources.getString(R.string.msgAreYouSure),
                DialogInterface.OnClickListener { adialog, _ ->
                    var slSalaryTarget : Salary? = null

                    StaticCollections.mastSalary?.forEach {
                        if(MathService.compareDateObjects(it.mdtDate, MathService.stringToCalendarTime(salaryTarget.mstrDate)) && it.msValue == salaryTarget.msValue
                        && it.mstSource == salaryTarget.mstrSource) {
                            slSalaryTarget = it
                        }
                    }

                    if(slSalaryTarget != null) {
                        meaSalaryAdapter.mltSalaryList.removeAt(nPosition)
                        meaSalaryAdapter.notifyItemRemoved(nPosition)
                        StaticCollections.mastSalary?.remove(slSalaryTarget!!)
                        SalarySharedPreferences.updateSalaryList(mctxContext)
                    }
                    adialog.dismiss()
                },
                DialogInterface.OnClickListener { adialog, _ ->
                    meaSalaryAdapter.notifyItemChanged(nPosition)
                    adialog.dismiss()
                })
    }
}