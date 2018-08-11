package diiin.model


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

/**
 * Define the model of salary in the system.
 *
 * @author Gabriel Moro
 *
 */
@Entity(tableName = "salary")
data class Salary(
        @PrimaryKey(autoGenerate = true) var mnID : Int?,
        @ColumnInfo(name = "value") var msValue : Float?,
        @ColumnInfo(name = "source") var mstrSource : String,
        @ColumnInfo(name = "date") var mstrDate : String)


/**
 * Define the model of expense in the system.
 *
 * @author Gabriel Moro
 */
@Entity(tableName = "expense",
        foreignKeys = [ForeignKey(entity = ExpenseType::class,
                parentColumns = arrayOf("mnExpenseTypeID"),
                childColumns = arrayOf("type"),
                onDelete = CASCADE)])
data class Expense(
        @PrimaryKey(autoGenerate = true) var mnID : Long?,
        @ColumnInfo(name = "value") var msValue : Float?,
        @ColumnInfo(name = "description") var mstrDescription : String,
        @ColumnInfo(name = "date") var mstrDate : String,
        @ColumnInfo(name = "type") var mnExpenseType : Long?
)

/**
 *
 */
@Entity(tableName = "expense_type")
data class ExpenseType(
        @PrimaryKey(autoGenerate = true) var mnExpenseTypeID : Long?,
        @ColumnInfo(name = "description") var mstrDescription : String,
        @ColumnInfo(name = "color") val mstrColor : String)