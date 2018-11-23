package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Expense
import diiin.model.ExpenseType
import io.reactivex.Maybe

/**
 * This interface defines the base operations over expense table.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Dao
interface ExpenseDAO {

    @Query("SELECT * FROM expense WHERE mnID =:anLong")
    fun getExpensesAccordingID(vararg anLong: Long): Maybe<Expense>

    /**
     * Get all elements from expense table.
     */
    @Query("SELECT * FROM expense")
    fun all(): Maybe<List<Expense>>

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

