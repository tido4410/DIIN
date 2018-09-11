package diiin.dao

import android.arch.persistence.room.Room
import android.content.Context
import diiin.model.Expense
import diiin.model.ExpenseType
import diiin.model.Salary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * This class provides the interfaces to access the data base manager and
 * the requests to find data in data base using the data base manager.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
class LocalCacheManager(actxContext: Context) {

    companion object {
        const val DATA_BASE_NAME: String = "diin-database"
    }

    val mappDataBaseBuilder: DataBaseFactory = Room.databaseBuilder(actxContext,
            DataBaseFactory::class.java, DATA_BASE_NAME)
            .build()

    /**
     * Return all data from expense table
     */
    fun getAllExpenses(adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpensesLoaded(it)
                }
    }

    /**
     * Return all data from expense type table.
     */
    fun getAllExpenseTypeObjects(adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpenseTypeLoaded(it)
                }
    }

    /**
     * Return all salaries from salary table.
     */
    fun getAllSalaries(adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.salaryDao()
                .all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onSalariesLoaded(it)
                }
    }

    /**
     * Return the salary according to specific id.
     */
    fun getSalaryAccordingId(anId: Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.salaryDao()
                .getSalaryAccordingID(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onSalaryObjectByIdReceived(it)
                }
    }

    /**
     * Return a specific expense according to its id.
     */
    fun getExpenseAccordingId(anLong: Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseDao()
                .getExpensesAccordingID(anLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adtDataBaseCallback.onExpenseIdReceived(it)
                }
    }

    /**
     * Return the expense type color according some id.
     */
    fun getExpenseTypeColor(anId: Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getColor(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeColorReceived(it) }
    }

    /**
     * Return expense type description according some id.
     */
    fun getExpenseTypeDescription(anId: Long, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getDescription(anId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeDescriptionReceived(it) }
    }

    /**
     * Return expense type object according some ID.
     */
    fun getExpenseTypeID(astrDescription: String, adtDataBaseCallback: DatabaseCallBack) {
        mappDataBaseBuilder.expenseTypeDao()
                .getId(astrDescription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adtDataBaseCallback.onExpenseTypeIDReceived(it) }
    }


    /**
     * This interface represents the callback object used to
     * represent the response from data base requests.
     *
     * @author Gabriel Moro
     * @since 24/08/2018
     * @version 1.0.9
     */
    interface DatabaseCallBack {
        /**
         * Result of getAllExpenses
         */
        fun onExpensesLoaded(alstExpenses: List<Expense>)

        /**
         * Result of getAllExpenseTypeObjects
         */
        fun onExpenseTypeLoaded(alstExpensesType: List<ExpenseType>)

        /**
         * Result of getAllSalaries
         */
        fun onSalariesLoaded(alstSalaries: List<Salary>)

        /**
         * Result of getExpenseAccordingId
         */
        fun onExpenseIdReceived(aexpense: Expense)

        /**
         * Result of getExpenseTypeColor
         */
        fun onExpenseTypeColorReceived(astrColor: String)

        /**
         * Result of getExpenseTypeDescription
         */
        fun onExpenseTypeDescriptionReceived(astrDescription: String)

        /**
         * Result of getExpenseTypeID
         */
        fun onExpenseTypeIDReceived(anID: Long?)

        /**
         * Result of getSalaryAccordingId
         */
        fun onSalaryObjectByIdReceived(aslSalary: Salary)
    }
}