package diiin.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Define the model of expense in the system.
 *
 * @author Gabriel Moro
 *
 * @param anId is the expense identifier
 * @param asValue is the expense price
 * @param asrDescription is something that user want to register
 * @param adtDate is the date of expense0
 * @param aetType is the type of expense
 */
class Expense(anId : Int?, asValue : Float?, asrDescription : String, adtDate : Date?, aetType : ExpenseType?) {

    var mnId : Int? = anId
    var msValue : Float? = asValue
    var msrDescription : String = asrDescription
    var mdtDate : Date? = adtDate
    val metType : ExpenseType? = aetType
}

@Entity(tableName = "expense")
data class ExpenseT(
        @PrimaryKey(autoGenerate = true) var mnID : Long?,
        @ColumnInfo(name = "value") var msValue : Float?,
        @ColumnInfo(name = "description") var mstrDescription : String,
        @ColumnInfo(name = "date") var mstrDate : String,
        @ColumnInfo(name = "type") var mnExpenseType : Long?)

@Entity(tableName = "expense_type")
data class ExpenseTType(
        @PrimaryKey(autoGenerate = true) var mnID : Long?,
        @ColumnInfo(name = "description") var mstrDescription : String,
        @ColumnInfo(name = "color") val mstrColor : String)