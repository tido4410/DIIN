package diiin.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import diiin.model.ExpenseT
import diiin.model.ExpenseTType

@Dao
interface ExpenseDAO {
    @Query("SELECT * From expense")
    fun all() : List<ExpenseT>

    @Insert
    fun add(vararg expense: ExpenseT)
}

@Dao
interface ExpenseTypeDAO {
    @Query("SELECT * From expense_type")
    fun all() : List<ExpenseTType>

    @Insert
    fun add(vararg expense: ExpenseTType)
}