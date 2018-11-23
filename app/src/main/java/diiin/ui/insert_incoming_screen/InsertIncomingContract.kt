package diiin.ui.insert_incoming_screen

import java.util.*

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface InsertIncomingContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun showSucessMessage()
        fun showUnsucessMessage()
        fun setDescription(astrDescription: String)
        fun setValue(astrValue: String)
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun loadSalaryValues(anSalaryID: Long?)
        fun saveSalary(anSalaryID: Long?, astrDescription: String, astrValue: String, adtDate: Date)
    }
}