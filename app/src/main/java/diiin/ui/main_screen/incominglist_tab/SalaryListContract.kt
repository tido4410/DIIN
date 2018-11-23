package diiin.ui.main_screen.incominglist_tab

import diiin.model.MonthType
import diiin.model.Incoming
import java.util.ArrayList

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface SalaryListContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun loadSalaryListAdapter(alstIncoming: ArrayList<Incoming>)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadSalaries(amnMonthSelected: MonthType, anYearSelected: Int)
    }
}