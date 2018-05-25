package com.example.gabrielbronzattimoro.diiin.model

import android.content.Context
import com.example.gabrielbronzattimoro.diiin.R
import java.util.*


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
        private val map = MonthType.values().associateBy(MonthType::aid)
        fun fromInt(type : Int) = map[type]

        fun gettingIdFromDescription(actxContext: Context, astrValue : String) : Int? {
            return when(astrValue) {
                JANUARY.description(actxContext) -> JANUARY.aid
                FEBRUARY.description(actxContext) -> FEBRUARY.aid
                MARCH.description(actxContext) -> MARCH.aid
                APRIL.description(actxContext) -> APRIL.aid
                MAY.description(actxContext) -> MAY.aid
                JUNE.description(actxContext) -> JUNE.aid
                JULY.description(actxContext) -> JULY.aid
                AUGUST.description(actxContext) -> AUGUST.aid
                SEPTEMBER.description(actxContext) -> SEPTEMBER.aid
                OCTOBER.description(actxContext) -> OCTOBER.aid
                NOVEMBER.description(actxContext) -> NOVEMBER.aid
                DECEMBER.description(actxContext) -> DECEMBER.aid
                else -> null
            }
        }
    }

    fun description(actxContext : Context) : String {
        return when(this) {
            JANUARY -> actxContext.resources.getString(R.string.january)
            FEBRUARY -> actxContext.resources.getString(R.string.february)
            MARCH -> actxContext.resources.getString(R.string.march)
            APRIL -> actxContext.resources.getString(R.string.april)
            MAY -> actxContext.resources.getString(R.string.may)
            JUNE -> actxContext.resources.getString(R.string.june)
            JULY -> actxContext.resources.getString(R.string.july)
            AUGUST -> actxContext.resources.getString(R.string.august)
            SEPTEMBER -> actxContext.resources.getString(R.string.september)
            OCTOBER -> actxContext.resources.getString(R.string.october)
            NOVEMBER -> actxContext.resources.getString(R.string.november)
            DECEMBER -> actxContext.resources.getString(R.string.december)
        }
    }

}