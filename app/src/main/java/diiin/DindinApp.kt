package diiin

import android.app.Application
import android.arch.persistence.room.Room
import diiin.dao.DataBaseFactory

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
    }

}