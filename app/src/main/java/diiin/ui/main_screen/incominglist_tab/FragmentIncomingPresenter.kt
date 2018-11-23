package diiin.ui.main_screen.incominglist_tab

import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.MonthType
import diiin.model.Incoming
import diiin.util.MathService
import java.util.*

/**
 * Presenter to SalaryListContract.View.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentIncomingPresenter(avwView: SalaryListContract.View) : SalaryListContract.Presenter {

    private val view: SalaryListContract.View = avwView

    /**
     * Load the salaries according to month and year selected for user.
     */
    override fun loadSalaries(amnMonthSelected: MonthType, anYearSelected: Int) {
        DindinApp.mlcmDataManager?.getAllSalaries(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {
                val lstIncomingFiltered: ArrayList<Incoming> = ArrayList()

                alstIncomings.forEach {
                    val clCalendar = Calendar.getInstance()
                    clCalendar.time = MathService.stringToCalendarTime(it.mstrDate, DindinApp.mstrDateFormat)
                    if (clCalendar.get(Calendar.MONTH) == amnMonthSelected.aid && clCalendar.get(Calendar.YEAR) == anYearSelected)
                        lstIncomingFiltered.add(it)
                }
                view.loadSalaryListAdapter(lstIncomingFiltered)
            }

            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
        })
    }

}
