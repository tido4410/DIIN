package diiin.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import diiin.model.*

@Database(entities = [Expense::class, Salary::class, ExpenseType::class], version = 1, exportSchema = false)
abstract class DataBaseFactory : RoomDatabase() {
    abstract fun expenseTypeDao() : ExpenseTypeDAO
    abstract fun expenseDao() : ExpenseDAO
    abstract fun salaryDao() : SalaryDAO
}