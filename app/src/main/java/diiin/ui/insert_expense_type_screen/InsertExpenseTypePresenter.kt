package diiin.ui.insert_expense_type_screen

import android.graphics.Color
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Incoming
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Presenter of InsertExpenseTypeScreen
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertExpenseTypePresenter(avwView: InsertExpenseTypeContract.View) : InsertExpenseTypeContract.Presenter {

    private var view: InsertExpenseTypeContract.View = avwView

    /**
     * Load all expenseType available in app
     */
    override fun loadExpenseTypeValues(anLongExpenseID: Long?) {
        anLongExpenseID ?: return

        DindinApp.mlcmDataManager?.getExpenseTypeColor(anLongExpenseID, object : LocalCacheManager.DatabaseCallBack {
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {
                val nColor = Color.parseColor(astrColor)
                view.setColor(nColor)
            }

            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
        })

        DindinApp.mlcmDataManager?.getExpenseTypeDescription(anLongExpenseID, object : LocalCacheManager.DatabaseCallBack {
            override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
            override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {
                view.setDecription(astrDescription)
            }

            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
        })
    }

    /**
     * Save a new expense type or some update.
     */
    override fun saveExpenseType(anExpenseTypeID: Long?, astrDescription: String, anColorSelected: Int) {
        val expenseTarget = ExpenseType(anExpenseTypeID, astrDescription, "#${Integer.toHexString(anColorSelected)}", R.drawable.food)
        if (astrDescription.isEmpty()) {
            view.showUnsucessMessage()
        } else {

            Observable.just(anExpenseTypeID != null).subscribeOn(Schedulers.io()).subscribe {
                if (it)
                    DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.update(expenseTarget)
                else
                    DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(expenseTarget)
            }
            view.showSucessMessage()
        }
    }

}