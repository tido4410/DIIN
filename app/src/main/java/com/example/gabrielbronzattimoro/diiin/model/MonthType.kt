package com.example.gabrielbronzattimoro.diiin.model

import android.content.Context
import com.example.gabrielbronzattimoro.diiin.R
import java.util.*

/**
 * Define the months of year
 *
 * @author Gabriel Moro
 *
 * @param aid is the month type identifier
 */
enum class MonthType(val aid : Int) {
    JANUARY(Calendar.JANUARY),
    FEBRUARY(Calendar.FEBRUARY),
    MARCH(Calendar.MARCH),
    APRIL(Calendar.APRIL),
    MAY(Calendar.MAY),
    JUNE(Calendar.JUNE),
    JULY(Calendar.JULY),
    AUGUST(Calendar.AUGUST),
    SEPTEMBER(Calendar.SEPTEMBER),
    OCTOBER(Calendar.OCTOBER),
    NOVEMBER(Calendar.NOVEMBER),
    DECEMBER(Calendar.DECEMBER);

    companion object {
        /**
         * This map connect the id and types
         */
        private val map = MonthType.values().associateBy(MonthType::aid)

        /**
         * Return some month type according to integer id
         * @param type is the integer id
         */
        fun fromInt(type : Int) = map[type]

        /**
         *
         * The method returns the month type id according some string
         * description.
         *
         * @param actContext is the screen context
         * @param astValue is the description of the expense type
         */
        fun gettingIdFromDescription(actContext: Context, astValue : String) : Int? {
            return when(astValue) {
                JANUARY.description(actContext) -> JANUARY.aid
                FEBRUARY.description(actContext) -> FEBRUARY.aid
                MARCH.description(actContext) -> MARCH.aid
                APRIL.description(actContext) -> APRIL.aid
                MAY.description(actContext) -> MAY.aid
                JUNE.description(actContext) -> JUNE.aid
                JULY.description(actContext) -> JULY.aid
                AUGUST.description(actContext) -> AUGUST.aid
                SEPTEMBER.description(actContext) -> SEPTEMBER.aid
                OCTOBER.description(actContext) -> OCTOBER.aid
                NOVEMBER.description(actContext) -> NOVEMBER.aid
                DECEMBER.description(actContext) -> DECEMBER.aid
                else -> null
            }
        }
    }

    /**
     * This method returns the description according month type
     * defined by constructor
     *
     * @param actContext is the screen context
     */
    fun description(actContext : Context) : String {
        return when(this) {
            JANUARY -> actContext.resources.getString(R.string.january)
            FEBRUARY -> actContext.resources.getString(R.string.february)
            MARCH -> actContext.resources.getString(R.string.march)
            APRIL -> actContext.resources.getString(R.string.april)
            MAY -> actContext.resources.getString(R.string.may)
            JUNE -> actContext.resources.getString(R.string.june)
            JULY -> actContext.resources.getString(R.string.july)
            AUGUST -> actContext.resources.getString(R.string.august)
            SEPTEMBER -> actContext.resources.getString(R.string.september)
            OCTOBER -> actContext.resources.getString(R.string.october)
            NOVEMBER -> actContext.resources.getString(R.string.november)
            DECEMBER -> actContext.resources.getString(R.string.december)
        }
    }

}