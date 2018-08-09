package diiin

import android.app.Application
import android.arch.persistence.room.Room
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
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.food), "#d74902"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.transport), "#af9825"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.phone), "#4591dc"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.pets), "#d74902"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.education), "#2c7308"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.health), "#810d07"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.funn), "#3eaeac"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.rent), "#a80fd2"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.travel), "#ff8e8e"))
            expenseDAO.add(ExpenseType(null, resources.getString(R.string.other), "#974646"))
        }
    }

}