package diiin.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides some services to app.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
object MathService {

    /**
     * Verify if the date is in the current year.
     */
    fun isTheDateInCurrentYear(dtDate : Date) : Boolean {
        val targetCalendar = Calendar.getInstance()
        targetCalendar.time = dtDate
        val currentCalendar = Calendar.getInstance()
        return (currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR))
    }

    /**
     * Format the float number to string to show like text.
     */
    fun formatFloatToCurrency(asValue : Float) : String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatter.format(asValue)
    }

    /**
     * Convert the string value to float number.
     */
    fun formatCurrencyValueToFloat(astValue : String) : Float? {
        return astValue.replace("R$","")
                .replace(".", "")
                .replace(",", ".").toFloatOrNull()
    }

    /**
     * Convert the integer value to string.
     */
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

    /**
     * Convert the date time object according to specific format.
     */
    fun calendarTimeToString(adt : Date, strFormat: String) : String {
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.format(adt)
    }

    /**
     * Convert the date string according specific format to date object.
     */
    fun stringToCalendarTime(astDate : String, strFormat : String) : Date {
        val formatter = SimpleDateFormat(strFormat, Locale("pt", "BR"))
        return formatter.parse(astDate)
    }

    /**
     * Convert string to float number.
     */
    fun stringToFloat(astrValue : String) : Float {
        return astrValue.replace(",",".").toFloat()
    }

}