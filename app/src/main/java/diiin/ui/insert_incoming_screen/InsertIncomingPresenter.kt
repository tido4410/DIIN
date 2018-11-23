package diiin.ui.insert_incoming_screen

import diiin.DindinApp
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Incoming
import diiin.util.MathService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Presenter of InsertIncomingContract.View
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class InsertIncomingPresenter(avwView: InsertIncomingContract.View) : InsertIncomingContract.Presenter {

    private var view: InsertIncomingContract.View = avwView

    /**
     * Load the last salary registry according to ID.
     * If the Id is not null, this load occurs.
     */
    override fun loadSalaryValues(anSalaryID: Long?) {
        if (anSalaryID != null) {
            DindinApp.mlcmDataManager?.getSalaryAccordingId(anSalaryID,
                    object : LocalCacheManager.DatabaseCallBack {
                        override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
                        override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {}
                        override fun onSalariesLoaded(alstIncomings: List<Incoming>) {}
                        override fun onExpenseIdReceived(aexpense: Expense) {}
                        override fun onExpenseTypeColorReceived(astrColor: String) {}
                        override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
                        override fun onExpenseTypeIDReceived(anID: Long?) {}
                        override fun onSalaryObjectByIdReceived(aslIncoming: Incoming) {
                            view.setDescription(aslIncoming.mstrSource)
                            if (aslIncoming.msValue != null) view.setValue(MathService.formatFloatToCurrency(aslIncoming.msValue!!))
                        }

                    })
        }
    }

    /**
     * Save the new salary or the update of it.
     */
    override fun saveSalary(anSalaryID: Long?, astrDescription: String, astrValue: String, adtDate: Date) {
        if (astrDescription.isEmpty() && astrValue.isEmpty()) {
            view.showUnsucessMessage()
        } else {
            val salaryTarget = Incoming(anSalaryID, MathService.formatCurrencyValueToFloat(astrValue), astrDescription, MathService.calendarTimeToString(adtDate, DindinApp.mstrDateFormat))
            Observable.just(anSalaryID != null).subscribeOn(Schedulers.io())
                    .subscribe {
                        if (it)
                            DindinApp.mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.update(salaryTarget)
                        else
                            DindinApp.mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.add(salaryTarget)
                    }
            view.showSucessMessage()
        }
    }

}