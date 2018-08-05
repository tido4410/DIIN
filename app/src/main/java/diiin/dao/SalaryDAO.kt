package diiin.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import diiin.model.Salary

@Dao
interface SalaryDAO {
    @Query("SELECT * FROM salary")
    fun all() : List<Salary>

    @Insert
    fun add(salaryT: Salary)

    @Delete
    fun delete(salaryT: Salary)
}