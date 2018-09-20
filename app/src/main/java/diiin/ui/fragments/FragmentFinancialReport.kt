package diiin.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.model.ExpenseType
import diiin.util.MathService
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.MonthType
import diiin.model.Salary
import diiin.ui.adapter.RefreshData
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface FinancialReportContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun drawPieChart(alstPieEntries: ArrayList<PieEntry>, alstColors: ArrayList<Int>)
        fun showChartItemCard(aetExpenseType: ExpenseType, asValue: Float)
        fun setSalaryTotal(astrValue: String)
        fun setExpenseTotal(astrValue: String)
        fun setWalletTotal(astrValue: String)
        fun hideChartItem()
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun dispareChartConstructor(amnMonthSelected: MonthType, anYearSelected: Int)
        fun onClickInPiePiece(peEntry: PieEntry)
        fun initPieEntry(asFloatPercent: Float, astrString: String): PieEntry
        fun loadDemonstrativePanel(amnMonthSelected: MonthType, anYearSelected: Int)
    }
}

/**
 * The presenter to FInancialReportContract.View
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FinancialReportPresenter(avwView: FinancialReportContract.View) : FinancialReportContract.Presenter {

    private val mhmExpenseByPercentage: HashMap<Long, Float> = HashMap()
    private val view: FinancialReportContract.View = avwView

    /**
     * Return the pie entry object with a float percent and label.
     */
    override fun initPieEntry(asFloatPercent: Float, astrString: String): PieEntry {
        val entry = PieEntry(asFloatPercent)
        entry.label = astrString
        return entry
    }

    /**
     * Load the financial demonstrative panel according the month and year selected.
     */
    override fun loadDemonstrativePanel(amnMonthSelected: MonthType, anYearSelected: Int) {
        var sTotalSalary = 0f
        DindinApp.mlcmDataManager?.getAllSalaries(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {
                alstSalaries.forEach {
                        val sValue = it.msValue ?: 0f
                        sTotalSalary += sValue
                }
                DindinApp.mlcmDataManager?.getAllExpenses(object : LocalCacheManager.DatabaseCallBack {
                    override fun onExpensesLoaded(alstExpenses: List<Expense>) {
                        var sTotalExpenseMonth = 0f
                        alstExpenses.forEach {
                            val clCalendar = Calendar.getInstance()
                            clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, DindinApp.mstrDateFormat)
                            if (clCalendar.get(Calendar.MONTH) == amnMonthSelected.aid && clCalendar.get(Calendar.YEAR) == anYearSelected) {
                                val sValue = it.msValue ?: 0f
                                sTotalExpenseMonth += sValue
                            }
                        }
                        view.setSalaryTotal(MathService.formatFloatToCurrency(sTotalSalary))
                        view.setExpenseTotal(MathService.formatFloatToCurrency(sTotalExpenseMonth))
                        val sWalletValue = sTotalSalary - sTotalExpenseMonth
                        view.setWalletTotal(MathService.formatFloatToCurrency(sWalletValue))
                        view.hideChartItem()
                    }

                    override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                    override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
                    override fun onExpenseIdReceived(aexpense: Expense) {}
                    override fun onExpenseTypeColorReceived(astrColor: String) {}
                    override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                    override fun onExpenseTypeIDReceived(anID: Long?) {}
                    override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
                })
            }

            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }

    /**
     * Create the new chart draft with the current state.
     */
    override fun dispareChartConstructor(amnMonthSelected: MonthType, anYearSelected: Int) {
        mhmExpenseByPercentage.clear()
        DindinApp.mlcmDataManager?.getAllExpenses(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {
                var sTotalExpenseMonth = 0f
                val lstExpensesOfMonth = ArrayList<Expense>()
                val lstEntries = ArrayList<PieEntry>()
                val lstColors = ArrayList<Int>()

                alstExpenses.forEach {
                    val clCalendar = Calendar.getInstance()
                    clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, DindinApp.mstrDateFormat)
                    if (clCalendar.get(Calendar.MONTH) == amnMonthSelected.aid && clCalendar.get(Calendar.YEAR) == anYearSelected) {
                        lstExpensesOfMonth.add(it)
                        if (it.msValue != null) sTotalExpenseMonth += it.msValue!!
                    }
                }
                lstExpensesOfMonth.sortWith(
                        Comparator { t0: Expense, t1: Expense ->
                            if (t0.mnExpenseType != null && t1.mnExpenseType != null) t0.mnExpenseType!!.compareTo(t1.mnExpenseType!!)
                            else {
                                when {
                                    t0.mnExpenseType == null -> -1
                                    t1.mnExpenseType == null -> 1
                                    else -> 0
                                }
                            }
                        }
                )
                var currentCategory: Long? = null
                var sValueCurrentCategory = 0f
                var nCount = 0
                val nSize = lstExpensesOfMonth.size

                while (nCount < nSize) {
                    val it = lstExpensesOfMonth[nCount]
                    val nextIt = if (nCount + 1 < nSize) lstExpensesOfMonth[nCount + 1] else null
                    if (currentCategory == null)
                        currentCategory = it.mnExpenseType

                    if (currentCategory == it.mnExpenseType) {
                        val sValue = it.msValue ?: 0f
                        sValueCurrentCategory += sValue
                    }

                    if (currentCategory != nextIt?.mnExpenseType || nextIt == null) {
                        if (DindinApp.mhmExpenseType != null) {
                            val expenseType = DindinApp.mhmExpenseType!![currentCategory]
                            if (expenseType != null) {
                                val strDescription = expenseType.mstrDescription
                                val strColor = expenseType.mstrColor
                                lstColors.add(if (strColor.isEmpty()) Color.parseColor("#ffff") else Color.parseColor(strColor))
                                lstEntries.add(initPieEntry(sValueCurrentCategory / sTotalExpenseMonth, strDescription))

                                mhmExpenseByPercentage[currentCategory!!] = sValueCurrentCategory
                                currentCategory = null
                                sValueCurrentCategory = 0f
                            }
                        }
                    }
                    nCount++
                }

                view.drawPieChart(lstEntries, lstColors)
            }

            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }

    /**
     * Define the event when user clicks in some piece of pie chart.
     */
    override fun onClickInPiePiece(peEntry: PieEntry) {
        DindinApp.mlcmDataManager?.getAllExpenses(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {
                alstExpenses.forEach {
                    if (it.mnExpenseType != null && DindinApp.mhmExpenseType != null) {
                        val expenseType = DindinApp.mhmExpenseType!![it.mnExpenseType!!]
                        if (expenseType != null && expenseType.mstrDescription == peEntry.label) {
                            val sValue = mhmExpenseByPercentage[expenseType.mnExpenseTypeID]
                            if (sValue != null)
                                view.showChartItemCard(expenseType, sValue)
                        }
                    }
                }
            }

            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }


}

/**
 * Screen that shows to user the financial overview
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentFinancialReport : Fragment(), RefreshData, FinancialReportContract.View {

    private var mpcPieChart: PieChart? = null
    private var mrlChartItem: RelativeLayout? = null
    private var mrlPieChartContainer: RelativeLayout? = null
    private var mrlWalletPanel: RelativeLayout? = null
    private var mtvExpenseTotalValue: TextView? = null
    private var mtvSalaryTotalValue: TextView? = null
    private var mtvWalletTotalValue: TextView? = null

    /**
     * Chart item elements
     */
    private var mtvChartItemValue: TextView? = null
    private var mtvChartItemDate: TextView? = null
    private var mvwChartItemExpenseType: View? = null
    private var mllChartItemLinearLayout: LinearLayout? = null
    private var mtvChartItemExpenseType: TextView? = null
    private var mivChartItemButtonMenu: ImageView? = null
    private var presenter: FinancialReportContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_financialreport, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mpcPieChart = view?.findViewById(R.id.pchart)
        mrlPieChartContainer = view?.findViewById(R.id.rlPieChart)
        mrlWalletPanel = view?.findViewById(R.id.rlWalletPanel)
        mtvExpenseTotalValue = view?.findViewById(R.id.tvExpenseTotalValue)
        mtvSalaryTotalValue = view?.findViewById(R.id.tvSalaryValue)
        mtvWalletTotalValue = view?.findViewById(R.id.tvWalletValue)
        mrlChartItem = view?.findViewById(R.id.rlChartItem)
        mtvChartItemValue = view?.findViewById(R.id.tvValue)
        mtvChartItemDate = view?.findViewById(R.id.tvDate)
        mtvChartItemExpenseType = view?.findViewById(R.id.tvExpenseType)
        mvwChartItemExpenseType = view?.findViewById(R.id.vwExpenseType)
        mllChartItemLinearLayout = view?.findViewById(R.id.llLine2)
        mivChartItemButtonMenu = view?.findViewById(R.id.ivMenuOption)

        presenter = FinancialReportPresenter(this)

        mpcPieChart?.setUsePercentValues(true)
        mpcPieChart?.description?.isEnabled = false
        mpcPieChart?.setExtraOffsets(2f, 5f, 2f, 2f)
        mpcPieChart?.dragDecelerationFrictionCoef = 0.95f
        mpcPieChart?.rotationAngle = 0f
        mpcPieChart?.isRotationEnabled = false
        mpcPieChart?.isDrawHoleEnabled = true
        mpcPieChart?.transparentCircleRadius = 10f
        mpcPieChart?.holeRadius = 7f
        mpcPieChart?.setHoleColor(ContextCompat.getColor(context, R.color.whiteColor))

    }

    override fun onResume() {
        super.onResume()

        mrlChartItem?.visibility = RelativeLayout.GONE
        mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
        refresh()
    }

    /**
     * Refresh method is used to show the current data state.
     */
    override fun refresh() {
        val mnMonthSelected = DindinApp.mmtMonthSelected ?: return
        val nYear = DindinApp.mnYearSelected ?: return
        presenter?.dispareChartConstructor(mnMonthSelected, nYear)
        presenter?.loadDemonstrativePanel(mnMonthSelected, nYear)
    }

    /**
     * Draw the pie chart according to pie entries list and colors arguments
     */
    override fun drawPieChart(alstPieEntries: ArrayList<PieEntry>, alstColors: ArrayList<Int>) {
        val dataSet = PieDataSet(alstPieEntries, "")
        dataSet.sliceSpace = 2f
        dataSet.formLineWidth = 120f
        dataSet.colors = alstColors
        val dataPie = PieData(dataSet)
        dataPie.setValueFormatter(PercentFormatter())
        dataPie.setValueTextSize(14f)
        dataPie.setValueTextColor(Color.TRANSPARENT)
        mpcPieChart?.legend?.textColor = ContextCompat.getColor(context, R.color.whiteColor)
        mpcPieChart?.legend?.textSize = 12f
        mpcPieChart?.legend?.isWordWrapEnabled = true
        mpcPieChart?.setDrawEntryLabels(false)
        mpcPieChart?.data = dataPie
        mpcPieChart?.highlightValues(null)
        mpcPieChart?.invalidate()

        mpcPieChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                mrlChartItem?.visibility = RelativeLayout.GONE
                mrlWalletPanel?.visibility = RelativeLayout.VISIBLE
            }

            override fun onValueSelected(e: Entry, h: Highlight?) {
                val pieEntry = e as PieEntry
                presenter?.onClickInPiePiece(pieEntry)
            }
        })
    }

    /**
     * Show the cell with more details according the piece selected for user.
     */
    override fun showChartItemCard(aetExpenseType: ExpenseType, asValue: Float) {
        mtvChartItemDate?.visibility = TextView.GONE
        mivChartItemButtonMenu?.visibility = ImageView.GONE
        mtvChartItemValue?.text = MathService.formatFloatToCurrency(asValue)
        mtvChartItemExpenseType?.text = aetExpenseType.mstrDescription
        mvwChartItemExpenseType?.setBackgroundColor(Color.parseColor(aetExpenseType.mstrColor))
        mrlChartItem?.visibility = RelativeLayout.VISIBLE
        mrlWalletPanel?.visibility = RelativeLayout.GONE
    }

    /**
     * Change the salary text value
     */
    override fun setSalaryTotal(astrValue: String) {
        mtvSalaryTotalValue?.text = astrValue
    }

    /**
     * Change the expense text value
     */
    override fun setExpenseTotal(astrValue: String) {
        mtvExpenseTotalValue?.text = astrValue
    }

    /**
     * Change the wallet text value
     */
    override fun setWalletTotal(astrValue: String) {
        mtvWalletTotalValue?.text = astrValue
    }

    /**
     * Hide the chart item card.
     */
    override fun hideChartItem() {
        mrlChartItem?.visibility = RelativeLayout.GONE
    }


}