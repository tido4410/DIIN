package diiin.ui.main_screen.expenselist_tab

import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.MonthType
import diiin.model.Incoming
import diiin.util.MathService
import java.util.*

/**
 * Define the presenter to expense list fragment.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class FragmentExpenseListPresenter(avwView: ExpenseListContract.View) : ExpenseListContract.Presenter {

    private val view: ExpenseListContract.View = avwView

    /**
     * Load all expenses according to month and year selected for user.
     */
    override fun loadExpenses(amnMonthSelected: MonthType, anYearSelected: Int) {
        DindinApp.mlcmDataManager?.getAllExpenses(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {

                val lstFilteredList: ArrayList<Expense> = ArrayList()

                alstExpenses.forEach { expense ->
                    val clCalendar = Calendar.getInstance()
                    clCalendar.time = MathService.stringToCalendarTime(expense.mstrDate, DindinApp.mstrDateFormat)
                    if (clCalendar.get(Calendar.MONTH) == amnMonthSelected.aid && clCalendar.get(Calendar.YEAR) == anYearSelected) {
                        lstFilteredList.add(expense)
                    }
                }
                view.loadExpenseListAdapter(lstFilteredList)
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