package diiin.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import diiin.model.Salary

@Dao
interface SalaryDAO {
    @Query("SELECT * FROM salary")
    fun all() : List<Salary>

    @Insert
    fun add(salaryT: Salary)

    @Delete
    fun delete(salaryT: Salary)

    @Update(onConflict = REPLACE)
    fun update(vararg salary: Salary)
}