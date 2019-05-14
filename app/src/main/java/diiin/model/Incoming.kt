package diiin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define the model of salary in the system.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
@Entity(tableName = "salary")
data class Incoming(
        @PrimaryKey(autoGenerate = true) var mnID: Long?,
        @ColumnInfo(name = "value") var msValue: Float?,
        @ColumnInfo(name = "source") var mstrSource: String,
        @ColumnInfo(name = "date") var mstrDate: String)