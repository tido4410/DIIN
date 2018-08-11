package diiin

import android.app.Application
import android.arch.persistence.room.Room
import android.util.Log
import br.com.gbmoro.diiin.R
import diiin.dao.DataBaseFactory
import diiin.dao.ExpenseTypeDAO
import diiin.model.ExpenseType
import kotlin.math.exp

/**
 * The main class of application.
 *
 * @author Gabriel Moro
 */
class DindinApp : Application() {


    override fun onCreate() {
        super.onCreate()

//        /**
//         * Load salary preferences to start Static Collections data objects
//         */
//        SalarySharedPreferences.getSalaryList(this)
//        /**
//         * Load expenses preferences to start Static Collections data objects
//         */
//        ExpenseSharedPreferences.getExpensesList(this)
//        /**
//         * Load month preference to start Static Collections data objects
//         */
//        SelectionSharedPreferences.getSelectedMonth(this)

        StaticCollections.mappDataBuilder = Room.databaseBuilder(this,
                DataBaseFactory::class.java,
                "diin-database")
                .allowMainThreadQueries()
                .build()

        loadDefaultExpenseTypeIfDataBaseIsEmpty()
    }

    private fun loadDefaultExpenseTypeIfDataBaseIsEmpty() {
        val expenseDAO : ExpenseTypeDAO = StaticCollections.mappDataBuilder?.expenseTypeDao() ?: return
        if(expenseDAO.all().isEmpty()) {
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