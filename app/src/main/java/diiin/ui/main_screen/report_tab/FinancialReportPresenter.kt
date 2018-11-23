package diiin.ui.main_screen.report_tab

import android.graphics.Color
import com.github.mikephil.charting.data.PieEntry
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.MonthType
import diiin.model.Incoming
import diiin.util.MathService
import java.util.*

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
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {
                alstIncomings.forEach {
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
                    override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                    override fun onExpenseIdReceived(aexpense: Expense) {}
                    override fun onExpenseTypeColorReceived(astrColor: String) {}
                    override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                    override fun onExpenseTypeIDReceived(anID: Long?) {}
                    override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
                })
            }

            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
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
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
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
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
        })
    }


}