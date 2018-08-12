package diiin.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import diiin.model.Expense
import diiin.model.ExpenseType

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM expense")
    fun all() : List<Expense>

    @Insert
    fun add(vararg expense: Expense)

    @Delete
    fun delete(expense: Expense)
}

@Dao
interface ExpenseTypeDAO {
    @Query("SELECT * FROM expense_type")
    fun all() : List<ExpenseType>

    @Query("SELECT mnExpenseTypeID FROM expense_type WHERE description = :a_strDescription")
    fun getId(a_strDescription : String) : Long?

    @Query("SELECT description FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getDescription(a_nLongID : Long?) : String

    @Query("SELECT color FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getColor(a_nLongID : Long?) : String

    @Insert
    fun add(vararg expense: ExpenseType)

    @Delete
    fun delete(expenseType: ExpenseType)
}