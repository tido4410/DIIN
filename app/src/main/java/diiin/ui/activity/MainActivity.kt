package diiin.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.MonthType
import diiin.ui.adapter.RefreshData
import diiin.ui.adapter.ViewPagerAdapter
import diiin.ui.fragments.FragmentExpensesList
import diiin.ui.fragments.FragmentFinancialReport
import diiin.ui.fragments.FragmentSalaryList
import diiin.util.MessageDialog
import diiin.util.SelectionSharedPreferences
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Define a contract between view and presenter.
 * MVP pattern.
 * @author Gabriel Moro
 * @since 24/08/2018
 * @version 1.0.9
 */
interface MainScreenContract {
    /**
     * Define all view operations.
     */
    interface View {
        fun loadViewPageAdapter()
        fun loadSpinnersContent()
        fun showFragmentContent()
        fun getMonthSelected(): String
        fun getSpinnerMonthsList(): ArrayList<String>
    }

    /**
     * Define all operations connected to model layer
     */
    interface Presenter {
        fun saveMonthSelected(actxContext: Context)
        fun loadMonthSelected(actxContext: Context): Int
    }
}

/**
 * Presenter of MainActivityContract.View.
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class MainPresenter(avwMainView: MainScreenContract.View) : MainScreenContract.Presenter {

    private val view: MainScreenContract.View = avwMainView

    /**
     * Save the month selected for user.
     */
    override fun saveMonthSelected(actxContext: Context) {
        val idOfMonth = MonthType.gettingIdFromDescription(actxContext, view.getMonthSelected())
        DindinApp.mmtMonthSelected = if (idOfMonth != null) {
            val monthType = MonthType.fromInt(idOfMonth)
            monthType
        } else {
            null
        } ?: return

        SelectionSharedPreferences.insertMonthSelectPreference(actxContext, DindinApp.mmtMonthSelected)
        view.loadViewPageAdapter()
        view.showFragmentContent()
    }

    /**
     * Load the month selected for user in the last use.
     */
    override fun loadMonthSelected(actxContext: Context): Int {
        DindinApp.mmtMonthSelected ?: return (Calendar.getInstance().get(Calendar.MONTH) + 1)

        val strMonthValue = DindinApp.mmtMonthSelected?.description(actxContext)

        var nCount = 0
        val nSize = view.getSpinnerMonthsList().size
        while (nCount < nSize) {
            if (view.getSpinnerMonthsList()[nCount] == strMonthValue)
                break
            nCount++
        }
        return nCount
    }
}

/**
 * This screen is used by user to see the expenses and salary report by month of year.
 *
 * @author Gabriel Moro
 * @since 11/09/2018
 * @version 1.0.9
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainScreenContract.View {

    var mnvNavigation: BottomNavigationView? = null
    var mspMonthSelector: Spinner? = null
    private var mtvYearSelected: TextView? = null
    private var mltSpinnerMonthsList: ArrayList<String>? = null
    private var mMenuInflated: Menu? = null
    private var mvwViewPager: ViewPager? = null
    private var mPrevMenuItem: MenuItem? = null
    private var presenter: MainScreenContract.Presenter? = null

    var mbNeedSomeDataChanged : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mnvNavigation = findViewById(R.id.bnvNavigation)
        mspMonthSelector = findViewById(R.id.spMonthSelector)
        mtvYearSelected = findViewById(R.id.tvYearSelected)
        mvwViewPager = findViewById(R.id.vwPagerComponent)

        mnvNavigation?.setOnNavigationItemSelectedListener(this)

        presenter = MainPresenter(this)
        loadViewPageAdapter()
        loadSpinnersContent()
    }

    /**
     * Return the month text value selected for user.
     */
    override fun getMonthSelected(): String {
        return mspMonthSelector?.selectedItem.toString()
    }

    /**
     * ViewPager setup.
     */
    override fun loadViewPageAdapter() {
        val lstPages = ArrayList<Fragment>()
        lstPages.add(FragmentExpensesList())
        lstPages.add(FragmentFinancialReport())
        lstPages.add(FragmentSalaryList())

        mvwViewPager?.adapter = ViewPagerAdapter(supportFragmentManager, lstPages)
        mvwViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mPrevMenuItem?.isChecked = false

                val mTargetMenu = when (position) {
                    0 -> {
                        mnvNavigation?.menu?.findItem(R.id.navigation_expenses)
                    }
                    1 -> {
                        if(mbNeedSomeDataChanged) {
                            mvwViewPager?.adapter?.notifyDataSetChanged()
                            mbNeedSomeDataChanged = false
                        }
                        mnvNavigation?.menu?.findItem(R.id.navigation_piechart)
                    }
                    2 -> {
                        mnvNavigation?.menu?.findItem(R.id.navigation_salary)
                    }
                    else -> {
                        null
                    }
                }

                mTargetMenu?.isChecked = true
                mPrevMenuItem = mTargetMenu
            }
        })
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        mtvYearSelected?.text = DindinApp.mnYearSelected.toString()


        (application as DindinApp).bus()
                .toObservable()
                .subscribeOn(Schedulers.computation())
                .subscribe {
                    mbNeedSomeDataChanged = true
                }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_expenses -> {
                mvwViewPager?.currentItem = 0
                return true
            }
            R.id.navigation_piechart -> {
                mvwViewPager?.currentItem = 1
                return true
            }
            R.id.navigation_salary -> {
                mvwViewPager?.currentItem = 2
                return true
            }
        }
        return false
    }

    /**
     * Load the content of the spinner component.
     */
    override fun loadSpinnersContent() {
        mltSpinnerMonthsList = ArrayList()
        mltSpinnerMonthsList?.add(resources.getString(R.string.All))

        MonthType.values().forEach { mltSpinnerMonthsList?.add(it.description(this)) }

        val lstArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mltSpinnerMonthsList)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mspMonthSelector?.adapter = lstArrayAdapter
        mspMonthSelector?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter?.saveMonthSelected(baseContext)
            }
        }

        val nIndex = presenter?.loadMonthSelected(this) ?: 0
        mspMonthSelector?.setSelection(nIndex) //the content has more one element (all)
    }

    /**
     * Return the months list used in spinner component.
     */
    override fun getSpinnerMonthsList(): ArrayList<String> {
        if (mltSpinnerMonthsList != null) return mltSpinnerMonthsList!!
        else return ArrayList()
    }

    /**
     * Show the fragment content.
     */
    override fun showFragmentContent() {
        val nCurrentIndex = mvwViewPager?.currentItem
        if (nCurrentIndex != null) {
            val fgCurrentFragment = (mvwViewPager?.adapter as ViewPagerAdapter).getItem(nCurrentIndex)
            if (fgCurrentFragment is RefreshData)
                fgCurrentFragment.refresh()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val nIndex = mvwViewPager?.currentItem
        if (nIndex != null)
            ((mvwViewPager?.adapter as ViewPagerAdapter).getItem(nIndex) as RefreshData).refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.useroptions, menu)
        menuInflater?.inflate(R.menu.deleteoption, menu)
        mMenuInflated = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item ?: return false

        when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.menu_info -> {
                val strStringAboutApp = "DINDIN 1.0.${packageManager.getPackageInfo(packageName, 0).versionCode}"
                MessageDialog.showMessageDialog(this,
                        strStringAboutApp,
                        DialogInterface.OnClickListener { adialog, _ ->
                            adialog.dismiss()
                        },
                        DialogInterface.OnClickListener { adialog, _ ->
                            adialog.dismiss()
                        })
            }
            else -> {
            }
        }
        return true
    }
}

