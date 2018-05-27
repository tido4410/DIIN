package com.example.gabrielbronzattimoro.diiin

import android.app.Application
import com.example.gabrielbronzattimoro.diiin.util.ExpenseSharedPreferences
import com.example.gabrielbronzattimoro.diiin.util.SalarySharedPreferences
import com.example.gabrielbronzattimoro.diiin.util.SelectionSharedPreferences

class DindinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SalarySharedPreferences.getSalaryList(this)
        ExpenseSharedPreferences.getExpensesList(this)
        SelectionSharedPreferences.getSelectedMonth(this)
    }

}