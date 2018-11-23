package diiin.ui.insert_expense_type_screen

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface InsertExpenseTypeContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun showSucessMessage()
        fun showUnsucessMessage()
        fun setColor(anColorValue: Int)
        fun setDecription(astrDescription: String)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadExpenseTypeValues(anLongExpenseID: Long?)
        fun saveExpenseType(anExpenseTypeID: Long?, astrDescription: String, anColorSelected: Int)
    }
}