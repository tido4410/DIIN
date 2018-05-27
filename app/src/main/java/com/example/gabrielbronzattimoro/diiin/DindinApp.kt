package com.example.gabrielbronzattimoro.diiin

import android.app.Application
import com.example.gabrielbronzattimoro.diiin.dao.ExpenseSharedPreferences
import com.example.gabrielbronzattimoro.diiin.dao.SalarySharedPreferences
import com.example.gabrielbronzattimoro.diiin.dao.SelectionSharedPreferences

class DindinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SalarySharedPreferences.getSalaryList(this)
        ExpenseSharedPreferences.getExpensesList(this)
        SelectionSharedPreferences.getSelectedMonth(this)
    }

}