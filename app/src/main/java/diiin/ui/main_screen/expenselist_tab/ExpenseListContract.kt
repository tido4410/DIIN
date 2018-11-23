package diiin.ui.main_screen.expenselist_tab

import diiin.model.Expense
import diiin.model.MonthType

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface ExpenseListContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun loadExpenseListAdapter(alstExpenses: ArrayList<Expense>)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadExpenses(amnMonthSelected: MonthType, anYearSelected: Int)
    }
}