package diiin

import android.app.Application
import android.arch.persistence.room.Room
import diiin.dao.DataBaseFactory
import diiin.dao.ExpenseTypeDAO
import diiin.model.ExpenseType
import diiin.util.ExpenseSharedPreferences
import diiin.util.SalarySharedPreferences
import diiin.util.SelectionSharedPreferences

/**
 * The main class of application.
 *
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
class DindinApp : Application() {

    override fun onCreate() {
        super.onCreate()


        StaticCollections.mappDataBuilder = Room.databaseBuilder(this,
                DataBaseFactory::class.java,
                "diin-database")
                .allowMainThreadQueries()
                .build()

        /**
         * Load the sharedpreferences
         */
        SelectionSharedPreferences.getSelectedMonth(this)
        // Keepping the compability between previous and current version
        val lstSalary = SalarySharedPreferences.getSalaryList(this)
        lstSalary.forEach { StaticCollections.mappDataBuilder?.salaryDao()?.add(it) }
        // Keepping the compability between previous and current version
        val lstExpenses = ExpenseSharedPreferences.getExpensesList(this)
        lstExpenses.forEach { StaticCollections.mappDataBuilder?.expenseDao()?.add(it) }

        /**
         * Load default ExpenseType if DataBase is empty
         */
        val expenseDAO: ExpenseTypeDAO? = StaticCollections.mappDataBuilder?.expenseTypeDao()
        if (expenseDAO != null && expenseDAO.all().isEmpty()) {
            expenseDAO.add(ExpenseType(null, "Food", "#d74902"))
            expenseDAO.add(ExpenseType(null, "Transport", "#af9825"))
            expenseDAO.add(ExpenseType(null, "Phone", "#4591dc"))
            expenseDAO.add(ExpenseType(null, "Pets", "#d74902"))
            expenseDAO.add(ExpenseType(null, "Education", "#2c7308"))
            expenseDAO.add(ExpenseType(null, "Health", "#810d07"))
            expenseDAO.add(ExpenseType(null, "Funn", "#3eaeac"))
            expenseDAO.add(ExpenseType(null, "Rent", "#a80fd2"))
            expenseDAO.add(ExpenseType(null, "Travel", "#ff8e8e"))
            expenseDAO.add(ExpenseType(null, "Other", "#974646"))
        }
    }

}