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
import diiin.DindinApp
import diiin.StaticCollections
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertSalaryActivity
import diiin.ui.activity.MainActivity
import diiin.ui.adapter.SalaryListAdapter
import diiin.util.MathService
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
            activity.startActivity(Intent(activity, InsertSalaryActivity::class.java))
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

    override fun loadPageContent() {


        DindinApp.mlcmDataManager?.getAllSalaries(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) { }
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) { }
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {

                mrvSalaryList ?: return

                val lstSalaryFiltered : ArrayList<Salary> = ArrayList()
                val slAdapter : SalaryListAdapter

                if(StaticCollections.mmtMonthSelected == null) {
                    lstSalaryFiltered.addAll(alstSalaries)
                    slAdapter = SalaryListAdapter(lstSalaryFiltered, context)
                } else {
                    alstSalaries.forEach{
                        val clCalendar = Calendar.getInstance()
                        clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, StaticCollections.mstrDateFormat)
                        if(clCalendar.get(Calendar.MONTH)== StaticCollections.mmtMonthSelected?.aid && clCalendar.get(Calendar.YEAR) == StaticCollections.mnYearSelected)
                            lstSalaryFiltered.add(it)
                    }
                    slAdapter = SalaryListAdapter(lstSalaryFiltered, context)
                }
                mrvSalaryList?.adapter = slAdapter
            }
            override fun onExpenseIdReceived(aexpense: Expense) { }
            override fun onExpenseTypeColorReceived(astrColor: String) { }
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) { }
            override fun onExpenseTypeIDReceived(anID: Long?) { }
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) { }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvSalaryList?.adapter?.notifyDataSetChanged()
    }

}