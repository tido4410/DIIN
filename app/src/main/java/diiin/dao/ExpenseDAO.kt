package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Expense
import diiin.model.ExpenseType

/**
 * This interface defines the base operations over expense table.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Dao
interface ExpenseDAO {
    /**
     * Get all elements from expense table.
     */
    @Query("SELECT * FROM expense")
    fun all() : List<Expense>

    /**
     * Add some expense element.
     */
    @Insert
    fun add(vararg expense: Expense)

    @Update(onConflict = REPLACE)
    fun update(vararg expense: Expense)

    /**
     * Remove specific expense object.
     */
    @Delete
    fun delete(expense: Expense)
}

/**
 * This interface defines the base operations over expensetype table.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Dao
interface ExpenseTypeDAO {

    /**
     * Get all elements from expensetype table.
     */
    @Query("SELECT * FROM expense_type")
    fun all() : List<ExpenseType>

    @Query("SELECT mnExpenseTypeID FROM expense_type WHERE description = :a_strDescription")
    fun getId(a_strDescription : String) : Long?

    /**
     * Get description according to id
     */
    @Query("SELECT description FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getDescription(a_nLongID : Long?) : String

    /**
     * Get color according to expense id.
     */
    @Query("SELECT color FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getColor(a_nLongID : Long?) : String

    /**
     * Add some expense type element.
     */
    @Insert
    fun add(vararg expense: ExpenseType)

    /**
     * Update the expense type element.
     */
    @Update(onConflict = REPLACE)
    fun update(vararg expenseType : ExpenseType)

    /**
     * Remove specific expense type object.
     */
    @Delete
    fun delete(expenseType: ExpenseType)
}