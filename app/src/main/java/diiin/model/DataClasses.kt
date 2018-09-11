package diiin.model


import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

/**
 * Define the model of salary in the system.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Entity(tableName = "salary")
data class Salary(
        @PrimaryKey(autoGenerate = true) var mnID: Long?,
        @ColumnInfo(name = "value") var msValue: Float?,
        @ColumnInfo(name = "source") var mstrSource: String,
        @ColumnInfo(name = "date") var mstrDate: String)


/**
 * Define the model of expense in the system.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Entity(tableName = "expense",
        foreignKeys = [ForeignKey(entity = ExpenseType::class,
                parentColumns = arrayOf("mnExpenseTypeID"),
                childColumns = arrayOf("type"),
                onDelete = CASCADE)])
data class Expense(
        @PrimaryKey(autoGenerate = true) var mnID: Long?,
        @ColumnInfo(name = "value") var msValue: Float?,
        @ColumnInfo(name = "description") var mstrDescription: String,
        @ColumnInfo(name = "date") var mstrDate: String,
        @ColumnInfo(name = "type") var mnExpenseType: Long?)

/**
 * The expense type model has color, description, and id.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Entity(tableName = "expense_type")
data class ExpenseType(
        @PrimaryKey(autoGenerate = true) var mnExpenseTypeID: Long?,
        @ColumnInfo(name = "description") var mstrDescription: String,
        @ColumnInfo(name = "color") val mstrColor: String)