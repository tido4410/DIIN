package diiin.ui.activity

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
import diiin.model.MonthType
import diiin.StaticCollections
import diiin.ui.adapter.RefreshData
import diiin.ui.adapter.ViewPagerAdapter
import diiin.ui.fragments.FragmentExpensesList
import diiin.ui.fragments.FragmentFinancialReport
import diiin.ui.fragments.FragmentSalaryList
import diiin.util.MessageDialog
import diiin.util.SelectionSharedPreferences
import java.util.*

interface MainScreenContract {
    interface View {
        fun loadViewPageAdapter()
        fun loadSpinnersContent()
        fun showFragmentContent()
        fun getMonthSelected() : String
        fun getSpinnerMonthsList() : ArrayList<String>
    }
    interface Presenter {
        fun saveMonthSelected(actxContext : Context)
        fun loadMonthSelected(actxContext: Context) : Int
    }
}

class MainPresenter(avwMainView : MainScreenContract.View) : MainScreenContract.Presenter{

    private val mvwMainView : MainScreenContract.View = avwMainView

    override fun saveMonthSelected(actxContext : Context) {
        val idOfMonth = MonthType.gettingIdFromDescription(actxContext, mvwMainView.getMonthSelected())
        StaticCollections.mmtMonthSelected = if(idOfMonth != null) {
            val monthType = MonthType.fromInt(idOfMonth)
            monthType
        } else { null } ?: return

        SelectionSharedPreferences.insertMonthSelectPreference(actxContext, StaticCollections.mmtMonthSelected)
        mvwMainView.loadViewPageAdapter()
        mvwMainView.showFragmentContent()
    }

    override fun loadMonthSelected(actxContext: Context) : Int {
        StaticCollections.mmtMonthSelected ?: return Calendar.getInstance().get(Calendar.MONTH)

        val strMonthValue = StaticCollections.mmtMonthSelected?.description(actxContext)

        var nCount = 0
        val nSize = mvwMainView.getSpinnerMonthsList().size
        while(nCount < nSize) {
            if(mvwMainView.getSpinnerMonthsList()[nCount]==strMonthValue)
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
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainScreenContract.View {

            var mnvNavigation : BottomNavigationView? = null
            var mspMonthSelector : Spinner? = null
    private var mtvYearSelected : TextView? = null
    private var mltSpinnerMonthsList : ArrayList<String>? = null
    private var mMenuInflated : Menu? = null
    private var mvwViewPager : ViewPager? = null
    private var mPrevMenuItem : MenuItem? = null
    private var mPresenter : MainScreenContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mnvNavigation = findViewById(R.id.bnvNavigation)
        mspMonthSelector = findViewById(R.id.spMonthSelector)
        mtvYearSelected = findViewById(R.id.tvYearSelected)
        mvwViewPager = findViewById(R.id.vwPagerComponent)

        mnvNavigation?.setOnNavigationItemSelectedListener(this)

        mPresenter = MainPresenter(this)
        loadViewPageAdapter()
        loadSpinnersContent()
    }

    override fun getMonthSelected(): String {
        return mspMonthSelector?.selectedItem.toString()
    }

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

                val mTargetMenu = when(position) {
                    0 -> {
                        mnvNavigation?.menu?.findItem(R.id.navigation_expenses)
                    }
                    1 -> {
                        mvwViewPager?.adapter?.notifyDataSetChanged()
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

    override fun onResume() {
        super.onResume()

        mtvYearSelected?.text = StaticCollections.mnYearSelected.toString()
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
                mPresenter?.saveMonthSelected(baseContext)
            }
        }

        val nIndex = mPresenter?.loadMonthSelected(this) ?: 0
        mspMonthSelector?.setSelection(nIndex)
    }



    override fun getSpinnerMonthsList() : ArrayList<String>{
        if(mltSpinnerMonthsList!=null) return mltSpinnerMonthsList!!
        else return ArrayList()
    }
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
        if(nIndex != null)
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

        when(item.itemId) {
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
            else -> { }
        }
        return true
    }
}

