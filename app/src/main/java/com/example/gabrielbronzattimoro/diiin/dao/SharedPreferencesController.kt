package com.example.gabrielbronzattimoro.diiin.dao

import android.content.Context
import android.content.SharedPreferences
import com.example.gabrielbronzattimoro.diiin.StaticCollections
import com.example.gabrielbronzattimoro.diiin.model.Expense
import com.example.gabrielbronzattimoro.diiin.model.ExpenseType
import com.example.gabrielbronzattimoro.diiin.model.MonthType
import com.example.gabrielbronzattimoro.diiin.model.Salary
import com.example.gabrielbronzattimoro.diiin.util.MathService


object SharedPreferenceConnection {

    private val SHARED_PREFERENCE_NAME = "dindin"
    private val PRIVATE_MODE = 0

    fun selector(actxContext : Context, strKey : String, strDefaultValue : String) : String  {
        return actxContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).getString(strKey, strDefaultValue)
    }

    fun editor(actxContext : Context) : SharedPreferences.Editor  {
        return actxContext.getSharedPreferences(SHARED_PREFERENCE_NAME, PRIVATE_MODE).edit()
    }
}


object SelectionSharedPreferences {

    val SELECTION_PREFERENCE_KEY = "SelectionPreferenceKey"

    fun getSelectedMonth(actxContext: Context) : MonthType? {
        val strValue = SharedPreferenceConnection.selector(actxContext, SELECTION_PREFERENCE_KEY, "")
        if(strValue.isNotEmpty()) {
            val nId = MonthType.gettingIdFromDescription(actxContext, strValue)
            if(nId != null) {
                val monthSelected = MonthType.fromInt(nId)
                StaticCollections.mmtMonthSelected = monthSelected
                return monthSelected
            }
            return null
        }
        return null
    }

    fun insertMonthSelectPreference(actxContext: Context, amtMonthType: MonthType?) {
        amtMonthType ?: return
        SharedPreferenceConnection.editor(actxContext).putString(SELECTION_PREFERENCE_KEY,amtMonthType.description(actxContext)).commit()
        getSelectedMonth(actxContext)
    }
}

object SalarySharedPreferences {
    val SALARY_PREFERENCE_KEY = "SalaryKey"

    fun getSalaryList(actxContext: Context) : List<Salary> {
        val lstToReturn = ArrayList<Salary>()
        val strValue = SharedPreferenceConnection.selector(actxContext, SALARY_PREFERENCE_KEY, "")
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
        StaticCollections.mlstSalary = lstToReturn
        return lstToReturn
    }

    fun insertNewSalary(actxContext : Context, salaryObj : Salary) : Boolean {
        salaryObj.mstrSource ?: return false
        salaryObj.mdtDate ?: return false
        salaryObj.msValue ?: return false

        var strValueToPersist : String = SharedPreferenceConnection.selector(actxContext, SalarySharedPreferences.SALARY_PREFERENCE_KEY, "")

        if(strValueToPersist.isNotEmpty()) strValueToPersist += "-"

        strValueToPersist += "${salaryObj.msValue}|${salaryObj.mstrSource}" +
                "|${MathService.calendarTimeToString(salaryObj.mdtDate!!)}"

        SharedPreferenceConnection.editor(actxContext).putString(SalarySharedPreferences.SALARY_PREFERENCE_KEY,strValueToPersist).commit()
        SalarySharedPreferences.getSalaryList(actxContext)
        return true
    }

}

object ExpenseSharedPreferences {

    val EXPENSES_PREFERENCE_KEY = "ExpensesKey"

    fun getExpensesList(actxContext: Context) : List<Expense> {
        val lstToReturn = ArrayList<Expense>()
        val strValue = SharedPreferenceConnection.selector(actxContext, EXPENSES_PREFERENCE_KEY, "")
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
        StaticCollections.mlstExpenses = lstToReturn
        return lstToReturn
    }

    fun insertNewExpense(actxContext : Context, expenseObj : Expense) : Boolean {

        expenseObj.mexpenseType ?: return false
        expenseObj.mdtDate ?: return false
        expenseObj.msValue ?: return false

        val lstToExpenses = getExpensesList(actxContext)

        expenseObj.mnId = lstToExpenses.size

        var strValueToPersist : String = SharedPreferenceConnection.selector(actxContext, EXPENSES_PREFERENCE_KEY, "")
        if(strValueToPersist.isNotEmpty()) strValueToPersist += "-"

        strValueToPersist += "${expenseObj.mnId}|${MathService.floatToString(expenseObj.msValue!!)}" +
                "|${expenseObj.mstrDescription}|${MathService.calendarTimeToString(expenseObj.mdtDate!!)}" +
                "|${expenseObj.mexpenseType.idExpense}"

        SharedPreferenceConnection.editor(actxContext).putString(EXPENSES_PREFERENCE_KEY,strValueToPersist).commit()
        getExpensesList(actxContext)
        return true
    }

}