package diiin.dao

import android.arch.persistence.room.*
import diiin.model.ExpenseType
import io.reactivex.Maybe

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
    fun all(): Maybe<List<ExpenseType>>

    @Query("SELECT mnExpenseTypeID FROM expense_type WHERE description = :a_strDescription")
    fun getId(a_strDescription: String): Maybe<Long?>

    /**
     * Get description according to id
     */
    @Query("SELECT description FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getDescription(a_nLongID: Long?): Maybe<String>

    /**
     * Get color according to expense id.
     */
    @Query("SELECT color FROM expense_type WHERE mnExpenseTypeID = :a_nLongID")
    fun getColor(a_nLongID: Long?): Maybe<String>

    /**
     * Add some expense type element.
     */
    @Insert
    fun add(vararg expense: ExpenseType)

    /**
     * Update the expense type element.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg expenseType: ExpenseType)

    /**
     * Remove specific expense type object.
     */
    @Delete
    fun delete(expenseType: ExpenseType)
}