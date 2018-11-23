package diiin.ui.insert_expense_screen

import diiin.DindinApp
import diiin.dao.DataBaseFactory
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Incoming
import diiin.util.MathService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Presenter of InsertExpenseScreen.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertExpensePresenter(avwView: InsertExpenseContract.View) : InsertExpenseContract.Presenter {

    private var view: InsertExpenseContract.View = avwView
    var dataBaseFactory: DataBaseFactory? = DindinApp.mlcmDataManager?.mappDataBaseBuilder

    /**
     * If user wants to update some expense, this function
     * is called to get the values of the expense that will be
     * updated.
     */
    override fun loadExpenseValues(anExpenseId: Long?) {
        anExpenseId ?: return
        DindinApp.mlcmDataManager?.getExpenseAccordingId(anExpenseId,
                object : LocalCacheManager.DatabaseCallBack {
                    override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
                    override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                    override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                    override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                    override fun onExpenseIdReceived(aexpense: Expense) {
                        view.setDate(aexpense.mstrDate)
                        view.setDescription(aexpense.mstrDescription)
                        if (aexpense.msValue != null)
                            view.setValue(MathService.formatFloatToCurrency(aexpense.msValue!!))
                    }

                    override fun onExpenseTypeColorReceived(astrColor: String) {}
                    override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                    override fun onExpenseTypeIDReceived(anID: Long?) {}
                })
    }

    /**
     * This method load all expense types available in app: food, pets.
     * @param anExpenseId is used to select the current expense type used
     * for expense (when update expense)
     */
    override fun loadCategories(anExpenseId: Long?) {

        val lstCategories = ArrayList<String>()

        if (anExpenseId != null) {
            DindinApp.mlcmDataManager?.getExpenseAccordingId(anExpenseId, object : LocalCacheManager.DatabaseCallBack {
                override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                override fun onExpenseIdReceived(aexpense: Expense) {
                    val nExpenseTypeID = aexpense.mnExpenseType
                    DindinApp.mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
                        override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                        override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                            var nCount = 0
                            alstExpensesType.forEachIndexed { nIndex, expense ->
                                lstCategories.add(expense.mstrDescription)
                                if (expense.mnExpenseTypeID == nExpenseTypeID) nCount = nIndex
                            }
                            view.fillCategoriesInSpinnerContentAndSelectSomeone(lstCategories, nCount)
                        }

                        override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                        override fun onExpenseIdReceived(aexpense: Expense) {}
                        override fun onExpenseTypeColorReceived(astrColor: String) {}
                        override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                        override fun onExpenseTypeIDReceived(anID: Long?) {}
                        override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
                    })
                }

                override fun onExpenseTypeColorReceived(astrColor: String) {}
                override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                override fun onExpenseTypeIDReceived(anID: Long?) {}
                override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
            })
        } else {
            DindinApp.mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
                override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
                override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                    alstExpensesType.forEach { expenseType -> lstCategories.add(expenseType.mstrDescription) }
                    view.fillCategoriesInSpinnerContentAndSelectSomeone(lstCategories, 0)
                }

                override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                override fun onExpenseIdReceived(aexpense: Expense) {}
                override fun onExpenseTypeColorReceived(astrColor: String) {}
                override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                override fun onExpenseTypeIDReceived(anID: Long?) {}
            })
        }
    }

    /**
     * This function save the new expense or the expense update.
     * When expense update operation, the anExpenseId is not null.
     */
    override fun saveExpense(anExpenseId: Long?, astrCategory: String, astrDescription: String, astrValue: String, adtDate: Date) {
        if (astrValue.isEmpty()) {
            view.showUnsucessMessage()
        } else {
            val sValue = MathService.formatCurrencyValueToFloat(astrValue)
            val strDate = MathService.calendarTimeToString(adtDate, DindinApp.mstrDateFormat)

            DindinApp.mlcmDataManager?.getExpenseTypeID(astrCategory, object : LocalCacheManager.DatabaseCallBack {
                override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {}
                override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                override fun onExpenseIdReceived(aexpense: Expense) {}
                override fun onExpenseTypeColorReceived(astrColor: String) {}
                override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                override fun onExpenseTypeIDReceived(anID: Long?) {
                    val expenseTarget = Expense(anExpenseId, sValue, astrDescription, strDate, anID)
                    Observable.just(anExpenseId != null).subscribeOn(Schedulers.io()).subscribe {
                        if (it)
                            dataBaseFactory?.expenseDao()?.update(expenseTarget)
                        else
                            dataBaseFactory?.expenseDao()?.add(expenseTarget)
                    }
                    view.showSucessMessage()
                }
            })
        }
    }
}