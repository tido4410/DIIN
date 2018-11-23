package diiin.ui.insert_expense_screen

import java.util.*

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface InsertExpenseContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun showSucessMessage()
        fun showUnsucessMessage()
        fun setDescription(astrDescription: String)
        fun setDate(astrDate: String)
        fun setValue(astrValue: String)
        fun fillCategoriesInSpinnerContentAndSelectSomeone(alstValues: ArrayList<String>, anItem: Int)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadExpenseValues(anExpenseId: Long?)
        fun loadCategories(anExpenseId: Long?)
        fun saveExpense(anExpenseId: Long?, astrCategory: String, astrDescription: String, astrValue: String, adtDate: Date)
    }
}