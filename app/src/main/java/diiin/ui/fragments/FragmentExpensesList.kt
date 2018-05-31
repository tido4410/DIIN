package diiin.ui.fragments

import android.annotation.SuppressLint
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
import diiin.model.Expense
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertExpenseActivity
import diiin.ui.adapter.ExpenseListAdapter
import java.util.*
import kotlin.collections.ArrayList

class FragmentExpensesList : Fragment() {

    private var mspMonthSelector: Spinner? = null
    private var mrvExpenseList: RecyclerView? = null
    private var mbtInsertExpense: FloatingActionButton? = null
    private var mithItemHelperReference : ItemTouchHelper? = null

    companion object {
        const val NAME = "FragmentExpensesList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_expenseslist, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view?.findViewById(R.id.spMonthSelector)
        mrvExpenseList = view?.findViewById(R.id.rvExpenseList)
        mbtInsertExpense = view?.findViewById(R.id.btnaddExpense)
        mbtInsertExpense?.setOnClickListener {
            context.startActivity(Intent(context, InsertExpenseActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvExpenseList?.layoutManager = llManager
        if(mbtInsertExpense!=null)
            mrvExpenseList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpense!!))
    }

    override fun onResume() {
        super.onResume()
        loadExpenseList()
    }


    fun loadExpenseListAccordingEditMode() {
        mrvExpenseList?.adapter?.notifyDataSetChanged()
    }

    private fun loadTouchHelperListener(aeaExpenseAdapter: ExpenseListAdapter) {

        if(mithItemHelperReference != null) {
            mithItemHelperReference!!.attachToRecyclerView(null)
            mithItemHelperReference = null
        }

        mithItemHelperReference = ItemTouchHelper(ExpenseListTouchHelper(aeaExpenseAdapter))
        mithItemHelperReference!!.attachToRecyclerView(mrvExpenseList)
    }

    fun loadExpenseList() {
        val lstExpenses = StaticCollections.mastExpenses ?: return
        mrvExpenseList ?: return

        val lstFilteredList : ArrayList<Expense> = ArrayList()
        val elAdapter : ExpenseListAdapter

        if(StaticCollections.mmtMonthSelected == null) {
            lstFilteredList.addAll(lstExpenses)
            elAdapter = ExpenseListAdapter(context, lstFilteredList)
            mrvExpenseList?.adapter = elAdapter
            loadTouchHelperListener(elAdapter)
        } else {
            lstExpenses.forEach{
                val clCalendar = Calendar.getInstance()
                clCalendar.time = it.mdtDate
                if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid) {
                    lstFilteredList.add(it)
                }
            }
            elAdapter = ExpenseListAdapter(context, lstFilteredList)
            mrvExpenseList?.adapter = elAdapter
            loadTouchHelperListener(elAdapter)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvExpenseList?.adapter?.notifyDataSetChanged()
    }

}

class ExpenseListTouchHelper(aeaExpenseAdapter : ExpenseListAdapter) : ItemTouchHelper.Callback() {

    private val meaExpenseAdapter : ExpenseListAdapter = aeaExpenseAdapter

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val nDragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val nSwipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(nDragFlags, nSwipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

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
                Collections.swap(meaExpenseAdapter.mltExpenseList, nCount, nCount+1)
                nCount++
            }
        } else {
            var nCount = nFromPosition
            while (nCount > nToPosition) {
                Collections.swap(meaExpenseAdapter.mltExpenseList, nCount, nCount-1)
                nCount--
            }
        }
        meaExpenseAdapter.notifyItemMoved(nFromPosition, nToPosition)
        return true
    }

    private fun onItemDismiss(nPosition : Int) {

    }

}