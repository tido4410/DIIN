package diiin.ui.activity

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
import diiin.ui.adapter.ViewPagerAdapter
import diiin.ui.fragments.FragmentExpensesList
import diiin.ui.fragments.FragmentFinancialReport
import diiin.ui.fragments.FragmentSalaryList
import diiin.util.MessageDialog
import diiin.util.SelectionSharedPreferences

/**
 * This screen is used by user to see the expenses and salary report by month of year.
 *
 * @author Gabriel Moro
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

            var mnvNavigation : BottomNavigationView? = null
            var mspMonthSelector : Spinner? = null
    private var mtvYearSelected : TextView? = null
    private var mltSpinnerMonthsList : ArrayList<String>? = null
    private var mMenuInflated : Menu? = null
    private var mvwViewPager : ViewPager? = null
    private var mPrevMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mnvNavigation = findViewById(R.id.bnvNavigation)
        mspMonthSelector = findViewById(R.id.spMonthSelector)
        mtvYearSelected = findViewById(R.id.tvYearSelected)
        mvwViewPager = findViewById(R.id.vwPagerComponent)

        mnvNavigation?.setOnNavigationItemSelectedListener(this)

        loadViewPageAdapter()
        loadSpinnersContent()
    }

    private fun loadViewPageAdapter() {
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

                val mTargetMenu: MenuItem? = when {
                    position == 0 -> mnvNavigation?.menu?.findItem(R.id.navigation_expenses)
                    position == 1 -> mnvNavigation?.menu?.findItem(R.id.navigation_piechart)
                    position == 2 -> mnvNavigation?.menu?.findItem(R.id.navigation_salary)
                    else -> null
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

    private fun loadSpinnersContent() {
        mltSpinnerMonthsList = ArrayList()
        mltSpinnerMonthsList?.add(resources.getString(R.string.All))

        MonthType.values().forEach { mltSpinnerMonthsList?.add(it.description(this)) }

        val lstArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mltSpinnerMonthsList)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mspMonthSelector?.adapter = lstArrayAdapter
        mspMonthSelector?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idOfMonth = MonthType.gettingIdFromDescription(baseContext, mspMonthSelector?.selectedItem.toString())
                StaticCollections.mmtMonthSelected = if(idOfMonth != null) {
                    val monthType = MonthType.fromInt(idOfMonth)
                    monthType
                } else { null } ?: return

                SelectionSharedPreferences.insertMonthSelectPreference(baseContext, StaticCollections.mmtMonthSelected)
                loadViewPageAdapter()
                showFragmentContent()
            }
        }

        mltSpinnerMonthsList ?: return
        StaticCollections.mmtMonthSelected ?: return

        val strMonthValue = StaticCollections.mmtMonthSelected?.description(this)

        var nCount = 0
        val nSize = mltSpinnerMonthsList?.size
        while(nCount < nSize!!) {
            if(mltSpinnerMonthsList!![nCount]==strMonthValue)
                break
            nCount++
        }
        mspMonthSelector?.setSelection(nCount)
    }

    private fun showFragmentContent() {
        val nCurrentIndex = mvwViewPager?.currentItem
        if (nCurrentIndex != null) {
            val fgCurrentFragment = (mvwViewPager?.adapter as ViewPagerAdapter).getItem(nCurrentIndex)
            if (fgCurrentFragment is MainPageFragments)
                fgCurrentFragment.loadPageContent()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val nIndex = mvwViewPager?.currentItem
        if(nIndex != null)
            ((mvwViewPager?.adapter as ViewPagerAdapter).getItem(nIndex) as MainPageFragments).loadPageContent()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.useroptions, menu)
        menuInflater?.inflate(R.menu.deleteoption, menu)
        mMenuInflated = menu
        mMenuInflated?.findItem(R.id.menu_done)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item ?: return false

        when(item.itemId) {
            /*R.id.menu_clearalldata -> {
                MessageDialog.showMessageDialog(this,
                        resources.getString(R.string.msgAreYouSure),
                        DialogInterface.OnClickListener { adialog, _ ->
                            SharedPreferenceConnection.clearAllPreferences(this)
                            MessageDialog.showToastMessage(this, resources.getString(R.string.pleaseRestartTheApp))
                            adialog.dismiss()
                        },
                        DialogInterface.OnClickListener { adialog, _ ->
                            adialog.dismiss()
                        })
            }*/
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.menu_edit -> {
                StaticCollections.mbEditMode = true
                showFragmentContent()
                mMenuInflated?.findItem(R.id.menu_done)?.isVisible = true
                mMenuInflated?.findItem(R.id.menu_edit)?.isVisible = false
            }

            R.id.menu_done -> {
                StaticCollections.mbEditMode = false
                showFragmentContent()
                mMenuInflated?.findItem(R.id.menu_edit)?.isVisible = true
                mMenuInflated?.findItem(R.id.menu_done)?.isVisible = false
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

    interface MainPageFragments {
        fun loadPageContent()
    }

}

