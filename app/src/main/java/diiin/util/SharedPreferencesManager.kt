package com.example.gabrielbronzattimoro.diiin.util

import android.content.Context
import android.content.SharedPreferences
import com.example.gabrielbronzattimoro.diiin.StaticCollections
import com.example.gabrielbronzattimoro.diiin.model.Expense
import com.example.gabrielbronzattimoro.diiin.model.ExpenseType
import com.example.gabrielbronzattimoro.diiin.model.MonthType
import com.example.gabrielbronzattimoro.diiin.model.Salary


object SharedPreferenceConnection {

    private const val SHARED_PREFERENCE_NAME = "dindin"
    private const val PRIVATE_MODE = 0

    fun selector(actContext : Context, strKey : String, strDefaultValue : String) : String  {
        return actContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).getString(strKey, strDefaultValue)
    }

    fun editor(actxContext : Context) : SharedPreferences.Editor  {
        return actxContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).edit()
    }

    fun clearAllPreferences(actxContext: Context) {
        editor(actxContext).clear().commit()
    }
}


object SelectionSharedPreferences {

    private const val SELECTION_PREFERENCE_KEY = "SelectionPreferenceKey"

    fun getSelectedMonth(actContext: Context) : MonthType? {
        val strValue = SharedPreferenceConnection.selector(actContext, SELECTION_PREFERENCE_KEY, "")
        if(strValue.isNotEmpty()) {
            val nId = MonthType.gettingIdFromDescription(actContext, strValue)
            if(nId != null) {
                val monthSelected = MonthType.fromInt(nId)
                StaticCollections.mmtMonthSelected = monthSelected
                return monthSelected
            }
            return null
        }
        return null
    }

    fun insertMonthSelectPreference(actContext: Context, amtMonthType: MonthType?) {
        amtMonthType ?: return
        SharedPreferenceConnection.editor(actContext).putString(SELECTION_PREFERENCE_KEY,amtMonthType.description(actContext)).commit()
        getSelectedMonth(actContext)
    }
}

object SalarySharedPreferences {
    private const val SALARY_PREFERENCE_KEY = "SalaryKey"

    fun getSalaryList(actContext: Context) : List<Salary> {
        val lstToReturn = ArrayList<Salary>()
        val strValue = SharedPreferenceConnection.selector(actContext, SALARY_PREFERENCE_KEY, "")
        if(strValue.isNotEmpty()) {
            val lstStrSalary = strValue.split("-")
            lstStrSalary.forEach {
                val lstStrAttributes = it.split("|")
                if(lstStrAttributes.size > 2) {
                    val sValue = MathService.stringToFloat(lstStrAttributes[0])
                    val strDescription = lstStrAttributes[1]
                    val dtDate = MathService.stringToCalendarTime(lstStrAttributes[2])
                    lstToReturn.add(Salary(strDescription, sValue, dtDate))
                }
            }
        }
        StaticCollections.mastSalary = lstToReturn
        return lstToReturn
    }

    fun updateSalaryList(actContext: Context) {
        var strValueToPersist : String = ""

        var nCount = 0
        val nSize = StaticCollections.mastSalary?.size ?: 0

        while (nCount < nSize) {
            if(StaticCollections.mastSalary != null) {
                val salaryTmp = StaticCollections.mastSalary!![nCount]
                strValueToPersist += "${salaryTmp.msValue}|${salaryTmp.mstSource}" +
                        "|${MathService.calendarTimeToString(salaryTmp.mdtDate!!)}"
                if(nCount != nSize - 1)
                    strValueToPersist += "-"
            }
            nCount++
        }

        SharedPreferenceConnection.editor(actContext).putString(SALARY_PREFERENCE_KEY, strValueToPersist).commit()
        getSalaryList(actContext)
    }

    fun insertNewSalary(actContext : Context, salaryObj : Salary) : Boolean {
        salaryObj.mstSource ?: return false
        salaryObj.mdtDate ?: return false
        salaryObj.msValue ?: return false

        var strValueToPersist : String = SharedPreferenceConnection.selector(actContext, SALARY_PREFERENCE_KEY, "")

        if(strValueToPersist.isNotEmpty()) strValueToPersist += "-"

        strValueToPersist += "${salaryObj.msValue}|${salaryObj.mstSource}" +
                "|${MathService.calendarTimeToString(salaryObj.mdtDate!!)}"

        SharedPreferenceConnection.editor(actContext).putString(SALARY_PREFERENCE_KEY,strValueToPersist).commit()
        getSalaryList(actContext)
        return true
    }

}

object ExpenseSharedPreferences {

    private const val EXPENSES_PREFERENCE_KEY = "ExpensesKey"

    fun getExpensesList(actContext: Context) : List<Expense> {
        val lstToReturn = ArrayList<Expense>()
        val strValue = SharedPreferenceConnection.selector(actContext, EXPENSES_PREFERENCE_KEY, "")
        if(strValue.isNotEmpty()) {
            val lstStrExpenses = strValue.split("-")
            lstStrExpenses.forEach {
                val lstStrAttributes = it.split("|")
                if(lstStrAttributes.size > 4) {
                    val nId = lstStrAttributes[0].toInt()
                    val sValue = MathService.stringToFloat(lstStrAttributes[1])
                    val strDescription = lstStrAttributes[2]
                    val dtDate = MathService.stringToCalendarTime(lstStrAttributes[3])
                    val etExpenseType = ExpenseType.fromInt(lstStrAttributes[4].toInt())
                    lstToReturn.add(Expense(nId, sValue, strDescription, dtDate, etExpenseType))
                }
            }
        }
        StaticCollections.mastExpenses = lstToReturn
        return lstToReturn
    }

    fun updateExpenseList(actContext: Context) {
        var strValueToPersist : String = ""

        var nCount = 0
        val nSize = StaticCollections.mastExpenses?.size ?: 0

        while(nCount < nSize) {
            if(StaticCollections.mastExpenses != null) {
                val expenseTmp: Expense = StaticCollections.mastExpenses!![nCount]
                strValueToPersist += "${expenseTmp.mnId}|${MathService.floatToString(expenseTmp.msValue!!)}" +
                        "|${expenseTmp.msrDescription}|${MathService.calendarTimeToString(expenseTmp.mdtDate!!)}" +
                        "|${expenseTmp.metType?.idExpense}"
                if(nCount != nSize - 1)
                    strValueToPersist += "-"
            }
            nCount++
        }

        SharedPreferenceConnection.editor(actContext).putString(EXPENSES_PREFERENCE_KEY,strValueToPersist).commit()
        getExpensesList(actContext)
    }

    fun insertNewExpense(actContext : Context, expenseObj : Expense) : Boolean {

        expenseObj.metType ?: return false
        expenseObj.mdtDate ?: return false
        expenseObj.msValue ?: return false

        val lstToExpenses = getExpensesList(actContext)

        expenseObj.mnId = lstToExpenses.size

        var strValueToPersist : String = SharedPreferenceConnection.selector(actContext, EXPENSES_PREFERENCE_KEY, "")
        if(strValueToPersist.isNotEmpty()) strValueToPersist += "-"

        strValueToPersist += "${expenseObj.mnId}|${MathService.floatToString(expenseObj.msValue!!)}" +
                "|${expenseObj.msrDescription}|${MathService.calendarTimeToString(expenseObj.mdtDate!!)}" +
                "|${expenseObj.metType.idExpense}"

        SharedPreferenceConnection.editor(actContext).putString(EXPENSES_PREFERENCE_KEY,strValueToPersist).commit()
        getExpensesList(actContext)
        return true
    }

}