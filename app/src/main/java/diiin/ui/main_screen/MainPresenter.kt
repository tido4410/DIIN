package diiin.ui.main_screen

import android.content.Context
import diiin.DindinApp
import diiin.model.MonthType
import diiin.util.SelectionSharedPreferences
import java.util.*

/**
 * Presenter of MainActivityContract.View.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class MainPresenter(avwMainView: MainScreenContract.View) : MainScreenContract.Presenter {

    private val view: MainScreenContract.View = avwMainView

    /**
     * Save the month selected for user.
     */
    override fun saveMonthSelected(actxContext: Context) {
        val idOfMonth = MonthType.gettingIdFromDescription(actxContext, view.getMonthSelected())
        DindinApp.mmtMonthSelected = if (idOfMonth != null) {
            val monthType = MonthType.fromInt(idOfMonth)
            monthType
        } else {
            null
        } ?: return

        SelectionSharedPreferences.insertMonthSelectPreference(actxContext, DindinApp.mmtMonthSelected)
        view.loadViewPageAdapter()
        view.showFragmentContent()
    }

    /**
     * Load the month selected for user in the last use.
     */
    override fun loadMonthSelected(actxContext: Context): Int {
        DindinApp.mmtMonthSelected ?: return (Calendar.getInstance().get(Calendar.MONTH) + 1)

        val strMonthValue = DindinApp.mmtMonthSelected?.description(actxContext)

        var nCount = 0
        val nSize = view.getSpinnerMonthsList().size
        while (nCount < nSize) {
            if (view.getSpinnerMonthsList()[nCount] == strMonthValue)
                break
            nCount++
        }
        return nCount
    }
}