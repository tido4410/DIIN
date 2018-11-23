package diiin.ui.main_screen

import android.content.Context
import java.util.ArrayList

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface MainScreenContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun loadViewPageAdapter()
        fun loadSpinnersContent()
        fun showFragmentContent()
        fun getMonthSelected(): String
        fun getSpinnerMonthsList(): ArrayList<String>
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun saveMonthSelected(actxContext: Context)
        fun loadMonthSelected(actxContext: Context): Int
    }
}
