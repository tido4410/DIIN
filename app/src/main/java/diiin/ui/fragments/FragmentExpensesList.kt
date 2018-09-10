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
import diiin.DindinApp
import diiin.StaticCollections
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertExpenseActivity
import diiin.ui.activity.MainActivity
import diiin.ui.adapter.ExpenseListAdapter
import diiin.ui.adapter.RefreshData
import diiin.util.MathService
import java.util.*
import kotlin.collections.ArrayList


/**
 * Screen that shows to user the expenses filter and list
 *
 * @author Gabriel Moro
 */
class FragmentExpensesList : Fragment(), RefreshData {

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
        refresh()
    }

    override fun refresh() {
        DindinApp.mlcmDataManager?.getAllExpenses(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {

                mrvExpenseList ?: return

                val lstFilteredList : ArrayList<Expense> = ArrayList()
                val elAdapter : ExpenseListAdapter

                StaticCollections.mmtMonthSelected ?: return

                alstExpenses.forEach{ expense ->
                    val clCalendar = Calendar.getInstance()
                    clCalendar.time = MathService.stringToCalendarTime(expense.mstrDate, StaticCollections.mstrDateFormat)
                    if(clCalendar.get(Calendar.MONTH)==StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR)==StaticCollections.mnYearSelected) {
                        lstFilteredList.add(expense)
                    }
                }
                elAdapter = ExpenseListAdapter(context, lstFilteredList)
                mrvExpenseList?.adapter = elAdapter
            }
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) { }
            override fun onSalariesLoaded(alstSalaries: List<Salary>) { }
            override fun onExpenseIdReceived(aexpense: Expense) { }
            override fun onExpenseTypeColorReceived(astrColor: String) { }
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) { }
            override fun onExpenseTypeIDReceived(anID: Long?) { }
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) { }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvExpenseList?.adapter?.notifyDataSetChanged()
    }

}