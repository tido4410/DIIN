package diiin.util

import java.text.DecimalFormat
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

    fun formatDoubleTwoPlacesAfterComma(asValue : Float) : String {
        return DecimalFormat("#.##").format(asValue)
    }

    fun formatFloatToCurrency(asValue : Float) : String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatter.format(asValue)
    }

    fun formatCurrencyValueToFloat(astValue : String) : Float? {
        return astValue.replace("R$","")
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
            sFloatNumber = when {
                strNumber.length==1 -> "0.0$strNumber".toFloatOrNull() ?: 0f
                strNumber.length==2 -> "0.$strNumber".toFloatOrNull() ?: 0f
                else -> 0f
            }
            strMoney = formatter?.format(sFloatNumber) ?: ""
        }
        return strMoney
    }

    fun calendarTimeToString(adt : Date) : String {
        val strFormat = "dd/MM/yyyy"
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.format(adt)
    }

    fun stringToCalendarTime(astDate : String) : Date {
        val strFormat = "dd/MM/yyyy"
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.parse(astDate)
    }

    fun floatToString(asValue : Float) : String {
        return asValue.toString()
    }

    fun stringToFloat(astrValue : String) : Float {
        return astrValue.replace(",",".").toFloat()
    }

    fun compareDateObjects(a_dtDate1 : Date?, a_dtDate2 : Date?) : Boolean {
        return if(a_dtDate1 == null && a_dtDate2== null) true
        else if(a_dtDate1 == null || a_dtDate2 == null) false
        else {
            val clCalendar1 = GregorianCalendar()
            clCalendar1.time = a_dtDate1
            val clCalendar2 = GregorianCalendar()
            clCalendar2.time = a_dtDate2

            (clCalendar1.get(Calendar.MONTH) == clCalendar2.get(Calendar.MONTH)
                    && clCalendar1.get(Calendar.YEAR) == clCalendar2.get(Calendar.YEAR)
                    && clCalendar1.get(Calendar.DAY_OF_MONTH) == clCalendar2.get(Calendar.DAY_OF_MONTH))
        }
    }

}