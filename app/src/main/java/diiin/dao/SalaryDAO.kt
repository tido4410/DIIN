package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Salary
import io.reactivex.Maybe

@Dao
interface SalaryDAO {
    @Query("SELECT * FROM salary")
    fun all() : Maybe<List<Salary>>

    @Query("SELECT * FROM salary WHERE mnID =:anLong")
    fun getSalaryAccordingID(anLong : Long?) : Maybe<Salary>

    @Insert
    fun add(salaryT: Salary)

    @Delete
    fun delete(salaryT: Salary)

    @Update(onConflict = REPLACE)
    fun update(vararg salary: Salary)
}