package diiin.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import diiin.model.ExpenseT
import diiin.model.ExpenseTType
import diiin.model.SalaryT

@Database(entities = [ExpenseT::class, SalaryT::class, ExpenseTType::class], version = 1, exportSchema = false)
abstract class DataBaseFactory : RoomDatabase() {
    abstract fun expenseTypeDao() : ExpenseTypeDAO
    abstract fun expenseDao() : ExpenseDAO
    abstract fun salaryDao() : SalaryDAO
}