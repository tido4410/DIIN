package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Salary
import io.reactivex.Maybe

/**
 * This interface defines the base operations over salary table.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Dao
interface SalaryDAO {
    /**
     * Return all salaries from salary table
     */
    @Query("SELECT * FROM salary")
    fun all(): Maybe<List<Salary>>

    /**
     * Return some salary according some id
     */
    @Query("SELECT * FROM salary WHERE mnID =:anLong")
    fun getSalaryAccordingID(anLong: Long?): Maybe<Salary>

    /**
     * Insert some salary in salary table
     */
    @Insert
    fun add(salaryT: Salary)

    /**
     * Remove some salary registry.
     */
    @Delete
    fun delete(salaryT: Salary)

    /**
     * Update some salary object.
     */
    @Update(onConflict = REPLACE)
    fun update(vararg salary: Salary)
}