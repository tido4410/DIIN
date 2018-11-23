package diiin.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

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
                onDelete = ForeignKey.CASCADE)])
data class Expense(
        @PrimaryKey(autoGenerate = true) var mnID: Long?,
        @ColumnInfo(name = "value") var msValue: Float?,
        @ColumnInfo(name = "description") var mstrDescription: String,
        @ColumnInfo(name = "date") var mstrDate: String,
        @ColumnInfo(name = "type") var mnExpenseType: Long?)