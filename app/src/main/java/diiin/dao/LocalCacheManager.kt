package diiin.dao

import android.arch.persistence.room.Room
import android.content.Context
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LocalCacheManager(actxContext : Context){

    companion object {
        const val DATA_BASE_NAME : String = "diin-database"
    }

    val mappDataBaseBuilder : DataBaseFactory = Room.databaseBuilder(actxContext,
            DataBaseFactory::class.java, DATA_BASE_NAME)
            .build()

    fun getAllExpenses(adtDataBaseCallback : DatabaseCallBack) {
        mappDataBaseBuilder.expenseDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpensesLoaded(it)
                }
    }

    fun getAllExpenseTypeObjects(adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpenseTypeLoaded(it)
                }
    }

    fun getAllSalaries(adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.salaryDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onSalariesLoaded(it)
                }
    }

    fun getSalaryAccordingId(anId: Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.salaryDao()
                .getSalaryAccordingID(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onSalaryObjectByIdReceived(it)
                }
    }

    fun getExpenseAccordingId(anLong : Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseDao()
                .getExpensesAccordingID(anLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpenseIdReceived(it)
                }
    }

    fun getExpenseTypeColor(anId : Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getColor(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeColorReceived(it) }
    }

    fun getExpenseTypeDescription(anId : Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getDescription(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeDescriptionReceived(it) }
    }

    fun getExpenseTypeID(astrDescription: String, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getId(astrDescription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeIDReceived(it)  }
    }



    interface DatabaseCallBack {
        fun onExpensesLoaded(alstExpenses : List<Expense>)
        fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>)
        fun onSalariesLoaded(alstSalaries : List<Salary>)
        fun onExpenseIdReceived(aexpense : Expense)
        fun onExpenseTypeColorReceived(astrColor : String)
        fun onExpenseTypeDescriptionReceived(astrDescription : String)
        fun onExpenseTypeIDReceived(anID : Long?)
        fun onSalaryObjectByIdReceived(aslSalary : Salary)
    }
}