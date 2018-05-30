package br.com.gbmoro.diiin

import android.app.Application
import br.com.gbmoro.diiin.util.ExpenseSharedPreferences
import br.com.gbmoro.diiin.util.SalarySharedPreferences
import br.com.gbmoro.diiin.util.SelectionSharedPreferences

class DindinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SalarySharedPreferences.getSalaryList(this)
        ExpenseSharedPreferences.getExpensesList(this)
        SelectionSharedPreferences.getSelectedMonth(this)
    }

}