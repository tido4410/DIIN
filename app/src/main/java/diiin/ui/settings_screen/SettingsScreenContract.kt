package diiin.ui.settings_screen

import android.content.Context
import diiin.model.ExpenseType
import java.util.ArrayList

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface SettingsScreenContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun callExpenseTypeInsertScreen()
        fun setExpenseTypeList(alstArrayList: ArrayList<ExpenseType>)
        fun setYear(astrYear: String)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun saveYear(actxContext: Context, astrYear: String)
        fun loadYear()
        fun loadExpenseTypes()
    }
}