package diiin.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Define the model of salary in the system.
 *
 * @author Gabriel Moro
 *
 * @param astSource is the origin of the payment
 * @param asValue is the payment total value
 * @param adtDate is payment date
 */
class Salary(astSource : String?, asValue : Float?, adtDate : Date?) {

    var mstSource : String? = astSource
    var msValue : Float? = asValue
    var mdtDate : Date? = adtDate
}

@Entity(tableName = "salary")
data class SalaryT(
        @PrimaryKey(autoGenerate = true) var mnID : Int?,
        @ColumnInfo(name = "value") var msValue : Float?,
        @ColumnInfo(name = "source") var mstrSource : String,
        @ColumnInfo(name = "date") var mstrDate : String)