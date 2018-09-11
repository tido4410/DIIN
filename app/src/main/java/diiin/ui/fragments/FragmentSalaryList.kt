package diiin.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.MonthType
import diiin.model.Salary
import diiin.ui.RVWithFLoatingButtonControl
import diiin.ui.activity.InsertSalaryActivity
import diiin.ui.adapter.RefreshData
import diiin.ui.adapter.SalaryListAdapter
import diiin.util.MathService
import java.util.*

interface SalaryListContract {
    interface View {
        fun loadSalaryListAdapter(alstSalary : ArrayList<Salary>)
    }
    interface Presenter {
        fun loadSalaries(amnMonthSelected : MonthType, anYearSelected : Int)
    }
}

class FragmentSalaryPresenter(avwView : SalaryListContract.View) : SalaryListContract.Presenter {

    private val view : SalaryListContract.View = avwView

    override fun loadSalaries(amnMonthSelected: MonthType, anYearSelected: Int) {
        DindinApp.mlcmDataManager?.getAllSalaries(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {
                val lstSalaryFiltered: ArrayList<Salary> = ArrayList()

                    alstSalaries.forEach {
                        val clCalendar = Calendar.getInstance()
                        clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, DindinApp.mstrDateFormat)
                        if (clCalendar.get(Calendar.MONTH) == amnMonthSelected.aid && clCalendar.get(Calendar.YEAR) == anYearSelected)
                            lstSalaryFiltered.add(it)
                    }
                view.loadSalaryListAdapter(lstSalaryFiltered)
            }

            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }

}

/**
 * Screen that shows to user the incomes filter and salary list
 *
 * @author Gabriel Moro
 */
class FragmentSalaryList : Fragment(), RefreshData, SalaryListContract.View {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
    var mbtnInsertSalary: FloatingActionButton? = null
    private var presenter : SalaryListContract.Presenter? = null

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

        presenter = FragmentSalaryPresenter(this)

        val llManager = LinearLayoutManager(context)
        mrvSalaryList?.layoutManager = llManager
        if (mbtnInsertSalary != null)
            mrvSalaryList?.setOnTouchListener(RVWithFLoatingButtonControl(mbtnInsertSalary!!))
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun refresh() {
        val mnMonthSelected = DindinApp.mmtMonthSelected ?: return
        val mnYear = DindinApp.mnYearSelected ?: return

        presenter?.loadSalaries(mnMonthSelected, mnYear)

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mrvSalaryList?.adapter?.notifyDataSetChanged()
    }

    override fun loadSalaryListAdapter(alstSalary: ArrayList<Salary>) {
        val slAdapter = SalaryListAdapter(alstSalary, context)
        mrvSalaryList?.adapter = slAdapter
    }

}