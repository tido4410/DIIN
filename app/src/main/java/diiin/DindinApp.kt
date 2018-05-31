package diiin

import android.app.Application
import diiin.util.ExpenseSharedPreferences
import diiin.util.SalarySharedPreferences
import diiin.util.SelectionSharedPreferences

class DindinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SalarySharedPreferences.getSalaryList(this)
        ExpenseSharedPreferences.getExpensesList(this)
        SelectionSharedPreferences.getSelectedMonth(this)
    }

}