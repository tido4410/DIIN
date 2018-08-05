package diiin.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import diiin.model.SalaryT

@Dao
interface SalaryDAO {
    @Query("SELECT * FROM salary")
    fun all() : List<SalaryT>

    @Insert
    fun add(salaryT: SalaryT)
}