package diiin

import android.annotation.SuppressLint
import android.app.Application
import br.com.gbmoro.diiin.R
import diiin.dao.LocalCacheManager
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.MonthType
import diiin.model.Salary
import diiin.ui.RxBus
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
        var mlcmDataManager: LocalCacheManager? = null
        var mhmExpenseType: HashMap<Long, ExpenseType>? = null
        /**
         * Represents the month selected in the filter by user
         */
        var mmtMonthSelected: MonthType? = null
        /**
         * Represent the current year.
         */
        var mnYearSelected: Int? = null

        val mstrDateFormat: String = "dd-MM-yyyy"
    }

    @SuppressLint("CheckResult")
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
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                Observable.just(alstExpensesType.isEmpty()).subscribeOn(Schedulers.io())
                        .subscribe {
                            if (it) {
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null, resources.getString(R.string.food), "#d74902"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.phone), "#4591dc"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.pets), "#d74902"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.education), "#2c7308"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.health), "#810d07"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.funn), "#3eaeac"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.rent), "#a80fd2"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.travel), "#ff8e8e"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.transport), "#af9825"))
                                mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.add(ExpenseType(null,  resources.getString(R.string.others), "#974646"))
                            }
                            loadExpensesTypeHashMap()
                        }
            }

            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
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

    @SuppressLint("CheckResults")
    fun loadExpensesTypeHashMap() {
        mlcmDataManager?.getAllExpenseTypeObjects(object : LocalCacheManager.DatabaseCallBack {
            override fun onExpensesLoaded(alstExpenses: List<Expense>) {}
            @SuppressLint("CheckResult")
            override fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>) {
                Observable.just(true).subscribeOn(Schedulers.io())
                        .subscribe {
                            mhmExpenseType = HashMap()
                            alstExpensesType.forEach { expense ->
                                if (expense.mnExpenseTypeID != null)
                                    mhmExpenseType?.put(expense.mnExpenseTypeID!!, expense)
                            }

                        }
            }

            override fun onSalariesLoaded(alstSalaries: List<Salary>) {}
            override fun onExpenseIdReceived(aexpense: Expense) {}
            override fun onExpenseTypeColorReceived(astrColor: String) {}
            override fun onExpenseTypeDescriptionReceived(astrDescription: String) {}
            override fun onExpenseTypeIDReceived(anID: Long?) {}
            override fun onSalaryObjectByIdReceived(aslSalary: Salary) {}
        })
    }

    private var bus : RxBus? = null

    fun bus() : RxBus {
        if(bus==null)
            bus = RxBus()
        return bus!!
    }
}