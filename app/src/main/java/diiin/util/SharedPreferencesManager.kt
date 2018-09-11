package diiin.util

import android.content.Context
import android.content.SharedPreferences
import diiin.StaticCollections
import diiin.model.Expense
import diiin.model.MonthType
import diiin.model.Salary
import java.util.*

/**
 * Provides the sharedpreferences connection.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
object SharedPreferenceConnection {

    private const val SHARED_PREFERENCE_NAME = "dindin"
    private const val PRIVATE_MODE = 0

    fun selector(actContext: Context, strKey: String, strDefaultValue: String): String {
        return actContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).getString(strKey, strDefaultValue)
    }

    fun editor(actxContext: Context): SharedPreferences.Editor {
        return actxContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).edit()
    }
}

/**
 * Provides access to get and update specific preferences.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
object SelectionSharedPreferences {

    private const val KEY_MONTH = "month"
    private const val KEY_YEAR = "year"

    /**
     * Get the last month selected by User.
     */
    fun getSelectedMonth(actContext: Context): MonthType? {
        val strValue = SharedPreferenceConnection.selector(actContext, KEY_MONTH, "")
        if (strValue.isNotEmpty()) {
            val nId = MonthType.gettingIdFromDescription(actContext, strValue)
            if (nId != null) {
                val monthSelected = MonthType.fromInt(nId)
                StaticCollections.mmtMonthSelected = monthSelected
                return monthSelected
            }
            return null
        }
        return null
    }

    /**
     * Save the month selected by User.
     */
    fun insertMonthSelectPreference(actContext: Context, amtMonthType: MonthType?) {
        amtMonthType ?: return
        SharedPreferenceConnection.editor(actContext).putString(KEY_MONTH, amtMonthType.description(actContext)).commit()
        getSelectedMonth(actContext)
    }

    /**
     * Save the year selected by User.
     */
    fun insertYearSelectPreference(actContext: Context, anYear: Int?) {
        anYear ?: return
        SharedPreferenceConnection.editor(actContext).putString(KEY_YEAR, anYear.toString()).commit()
        getSelectedYear(actContext)
    }

    /**
     * Get the last year selected by User.
     */
    fun getSelectedYear(actContext: Context): Int? {
        val defaultYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val strValue = SharedPreferenceConnection.selector(actContext, KEY_YEAR, defaultYear)
        if (strValue.isNotEmpty()) {
            val nYear = strValue.toIntOrNull()
            if (nYear != null) {
                StaticCollections.mnYearSelected = nYear
                return nYear
            }
            return null
        }
        return null
    }
}

/**
 * Use to keep the compability between room and sharedpreferences
 */
object SalarySharedPreferences {
    private const val SALARY_PREFERENCE_KEY = "SalaryKey"

    fun getSalaryList(actContext: Context): List<Salary> {
        val lstToReturn = ArrayList<Salary>()
        val strValue = SharedPreferenceConnection.selector(actContext, SALARY_PREFERENCE_KEY, "")
        if (strValue.isNotEmpty()) {
            val lstStrSalary = strValue.split("-")
            lstStrSalary.forEach {
                val lstStrAttributes = it.split("|")
                if (lstStrAttributes.size > 2) {
                    val sValue = MathService.stringToFloat(lstStrAttributes[0])
                    val strDescription = lstStrAttributes[1]
                    val strDate = lstStrAttributes[2]
                    lstToReturn.add(Salary(null, sValue, strDescription, strDate))
                }
            }
        }
        return lstToReturn
    }
}

/**
 * Use to keep the compability between room and sharedpreferences
 */
object ExpenseSharedPreferences {

    private const val EXPENSES_PREFERENCE_KEY = "ExpensesKey"

    fun getExpensesList(actContext: Context): List<Expense> {
        val lstToReturn = ArrayList<Expense>()
        val strValue = SharedPreferenceConnection.selector(actContext, EXPENSES_PREFERENCE_KEY, "")
        if (strValue.isNotEmpty()) {
            val lstStrExpenses = strValue.split("-")
            lstStrExpenses.forEach {
                val lstStrAttributes = it.split("|")
                if (lstStrAttributes.size > 4) {
                    val sValue = MathService.stringToFloat(lstStrAttributes[1])
                    val strDescription = lstStrAttributes[2]
                    val strDate = lstStrAttributes[3]
                    val nExpenseType = lstStrAttributes[4].toLongOrNull()
                    lstToReturn.add(Expense(null, sValue, strDescription, strDate, nExpenseType))
                }
            }
        }
        return lstToReturn
    }
}