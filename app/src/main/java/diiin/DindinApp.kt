package diiin

import android.annotation.SuppressLint
import android.app.Application
import android.arch.persistence.room.Room
import android.os.AsyncTask
import android.os.Handler
import diiin.dao.DataBaseFactory
import diiin.dao.ExpenseTypeDAO
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import diiin.util.ExpenseSharedPreferences
import diiin.util.SalarySharedPreferences
import diiin.util.SelectionSharedPreferences
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * The main class of application.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
class DindinApp : Application() {

    companion object {
        var mlcmDataManager : LocalCacheManager? = null
    }

    override fun onCreate() {
        super.onCreate()

        mlcmDataManager = LocalCacheManager(this)

        /**
         * Load the sharedpreferences
         */
        SelectionSharedPreferences.getSelectedMonth(this)
        SelectionSharedPreferences.getSelectedYear(this)

//        Observable.fromCallable {

        mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) { }
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                Observable.just(alstExpensesType.isEmpty()).subscribeOn(Schedulers.io())
                        .subscribe {
                            if(it) {
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Comida", "#d74902"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Telefone", "#4591dc"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Animais", "#d74902"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Educação", "#2c7308"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Saúde", "#810d07"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Lazer", "#3eaeac"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Aluguel", "#a80fd2"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Viagens", "#ff8e8e"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Transporte", "#af9825"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, "Outros", "#974646"))
                            }
                        }
            }
            override fun onSalariesLoaded(alstSalaries: List<Salary>) { }
            override fun onExpenseIdReceived(aexpense: Expense) { }
            override fun onExpenseTypeColorReceived(astrColor: String) { }
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) { }
            override fun onExpenseTypeIDReceived(anID: Long?) { }
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) { }
        })

        // Keepping the compability between previous and current version
        val lstSalary = SalarySharedPreferences.getSalaryList(this)
        val lstExpenses = ExpenseSharedPreferences.getExpensesList(this)
            Observable.just(true).subscribeOn(Schedulers.io())
                .subscribe {
                    lstSalary.forEach { salary -> mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.add(salary) }
                    lstExpenses.forEach { expense -> mlcmDataManager?.mappDataBaseBuilder?.expenseDao()?.add(expense) }
                }
    }
}