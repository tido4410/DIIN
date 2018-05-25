package com.example.gabrielbronzattimoro.diiin.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object MathService {

    fun isTheDateInCurrentYear(dtDate : Date) : Boolean {
        val targetCalendar = Calendar.getInstance()
        targetCalendar.time = dtDate
        val currentCalendar = Calendar.getInstance()
        return (currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR))
    }

    fun formatFloatToCurrency(asValue : Float) : String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatter.format(asValue)
    }

    fun formatCurrencyValueToFloat(astrValue : String) : Float? {
        return astrValue.replace("R$","")
                .replace(".", "")
                .replace(",", ".").toFloatOrNull()
    }

    fun formatIntToCurrencyValue(anNumber : Int) : String {
        val strNumber = anNumber.toString()
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val sFloatNumber : Float
        val strMoney : String

        if(strNumber.length > 2) {
            val strFirstPart = strNumber.substring(0, strNumber.length - 2)
            val strSecondPart = strNumber.substring(strNumber.length - 2, strNumber.length)
            sFloatNumber = "$strFirstPart.$strSecondPart".toFloatOrNull() ?: 0f
            strMoney = formatter?.format(sFloatNumber) ?: ""
        } else {
            sFloatNumber = if(strNumber.length==1) {
                "0.0$strNumber".toFloatOrNull() ?: 0f
            } else if(strNumber.length==2) {
                "0.$strNumber".toFloatOrNull() ?: 0f
            } else 0f
            strMoney = formatter?.format(sFloatNumber) ?: ""
        }
        return strMoney
    }

    fun calendarTimeToString(adt : Date) : String {
        val strFormat = "dd/MM/yyyy"
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.format(adt)
    }

    fun stringToCalendarTime(astrDate : String) : Date {
        val strFormat = "dd/MM/yyyy"
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.parse(astrDate)
    }

    fun floatToString(asValue : Float) : String {
        return asValue.toString()
    }

    fun stringToFloat(astrValue : String) : Float {
        return astrValue.replace(",",".").toFloat()
    }

}