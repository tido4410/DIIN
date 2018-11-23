package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Incoming
import io.reactivex.Maybe

/**
 * This interface defines the base operations over salary table.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Dao
interface IncomingDAO {
    /**
     * Return all salaries from salary table
     */
    @Query("SELECT * FROM salary")
    fun all(): Maybe<List<Incoming>>

    /**
     * Return some salary according some id
     */
    @Query("SELECT * FROM salary WHERE mnID =:anLong")
    fun getSalaryAccordingID(anLong: Long?): Maybe<Incoming>

    /**
     * Insert some salary in salary table
     */
    @Insert
    fun add(incomingT: Incoming)

    /**
     * Remove some salary registry.
     */
    @Delete
    fun delete(incomingT: Incoming)

    /**
     * Update some incoming object.
     */
    @Update(onConflict = REPLACE)
    fun update(vararg incoming: Incoming)
}