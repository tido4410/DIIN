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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Expense
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertExpenseActivity
import diiin.ui.activity.MainActivity
import diiin.ui.adapter.ExpenseListAdapter
//import diiin.util.ExpenseSharedPreferences
import diiin.util.MathService
import diiin.util.MessageDialog
import java.util.*
import kotlin.collections.ArrayList

/**
 * Screen that shows to user the expenses filter and list
 *
 * @author Gabriel Moro
 */
class FragmentExpensesList : Fragment(), MainActivity.MainPageFragments {

    private var mspMonthSelector: Spinner? = null
    private var mrvExpenseList: RecyclerView? = null
            var mbtInsertExpense: FloatingActionButton? = null

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
            activity.startActivity(Intent(activity, InsertExpenseActivity::class.java))
        }

        val llManager = LinearLayoutManager(context)
        mrvExpenseList?.layoutManager = llManager
        if(mbtInsertExpense!=null)
            mrvExpenseList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtInsertExpense!!))
    }

    override fun onResume() {
        super.onResume()
        loadPageContent()
    }

    override fun loadPageContent() {
        val lstExpenses = StaticCollections.mappDataBuilder?.expenseDao()?.all() ?: return
        mrvExpenseList ?: return

        val lstFilteredList : ArrayList<Expense> = ArrayList()
        val elAdapter : ExpenseListAdapter

        StaticCollections.mmtMonthSelected ?: return

        lstExpenses.forEach{
            val clCalendar = Calendar.getInstance()
            clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, StaticCollections.mstrDateFormat)
            if(clCalendar.get(Calendar.MONTH)==StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR)==StaticCollections.mnYearSelected) {
                lstFilteredList.add(it)
                Log.d("DBTT", "ID ${it.mnID} - DESC ${it.mnID}")
            }
        }
        elAdapter = ExpenseListAdapter(context, lstFilteredList)
        mrvExpenseList?.adapter = elAdapter
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvExpenseList?.adapter?.notifyDataSetChanged()
    }

}