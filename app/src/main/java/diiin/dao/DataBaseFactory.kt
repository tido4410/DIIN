package diiin.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import diiin.model.*

/**
 * This class provides the interfaces to access the data base tables.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 *
 * TODO "Alter table to add new column that contains the icon id. Change the data base version."
 */
@Database(entities = [Expense::class, Incoming::class, ExpenseType::class], version = 1, exportSchema = false)
abstract class DataBaseFactory : RoomDatabase() {

    /**
     * Provides expensetypedao
     */
    abstract fun expenseTypeDao(): ExpenseTypeDAO

    /**
     * Provides expensedao
     */
    abstract fun expenseDao(): ExpenseDAO

    /**
     * Provides salary dao
     */
    abstract fun salaryDao(): IncomingDAO
}