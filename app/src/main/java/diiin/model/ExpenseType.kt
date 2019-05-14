package diiin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
        @ColumnInfo(name = "color") val mstrColor: String,
        @ColumnInfo(name = "iconid") val mnIconIdResource: Int)