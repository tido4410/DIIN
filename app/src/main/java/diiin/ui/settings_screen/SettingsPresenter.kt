package diiin.ui.settings_screen

import android.content.Context
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Incoming
import diiin.util.SelectionSharedPreferences
import java.util.ArrayList
import java.util.HashMap

/**
 * Presenter of the SettingsScreen
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class SettingsPresenter(avwView: SettingsScreenContract.View) : SettingsScreenContract.Presenter {
    private val view: SettingsScreenContract.View = avwView

    /**
     * Save the year in shared preferences.
     */
    override fun saveYear(actxContext: Context, astrYear: String) {
        val nYear = astrYear.toIntOrNull() ?: return
        SelectionSharedPreferences.insertYearSelectPreference(actxContext, nYear)
    }

    /**
     * Load the last year defined for user.
     */
    override fun loadYear() {
        val strCurrentYear = DindinApp.mnYearSelected.toString()
        view.setYear(strCurrentYear)
    }

    /**
     * Load all expense types available in app.
     */
    override fun loadExpenseTypes() {
        DindinApp.mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                view.setExpenseTypeList(ArrayList(alstExpensesType))
                /**
                 * Update the global structure used to represent
                 * the expense types hashmap
                 */
                DindinApp.mhmExpenseType = HashMap()
                alstExpensesType.forEach { expense ->
                    if (expense.mnExpenseTypeID != null)
                        DindinApp.mhmExpenseType?.put(expense.mnExpenseTypeID!!, expense)
                }
            }

            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
        })
    }

}