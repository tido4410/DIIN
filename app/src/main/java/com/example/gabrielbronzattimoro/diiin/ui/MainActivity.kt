package com.example.gabrielbronzattimoro.diiin.ui

import android.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.gabrielbronzattimoro.diiin.dao.SelectionSharedPreferences
import com.example.gabrielbronzattimoro.diiin.model.MonthType
import com.example.gabrielbronzattimoro.diiin.StaticCollections


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    private var mbnvNavigation : BottomNavigationView? = null
    private var mstrCurrentFragment : String? = null
    private var mfgCurrentFragment : Fragment? = null
    private var mspMonthSelector : Spinner? = null
    private var mlstSpinnerMonthsList : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mbnvNavigation = findViewById(R.id.bnvNavigation)
        mspMonthSelector = findViewById(R.id.spMonthSelector)

        mbnvNavigation?.setOnNavigationItemSelectedListener(this)

        loadSpinnersContent()
    }

    override fun onResume() {
        super.onResume()

        if(mstrCurrentFragment == null)
            mstrCurrentFragment = FragmentExpensesList.TAGNAME

        if(mfgCurrentFragment != null)
            supportFragmentManager.beginTransaction().remove(mfgCurrentFragment).commit()

        mfgCurrentFragment = when(mstrCurrentFragment) {
            FragmentSalaryList.TAGNAME -> FragmentSalaryList()
            FragmentFinancialReport.TAGNAME -> FragmentFinancialReport()
            FragmentExpensesList.TAGNAME -> FragmentExpensesList()
            else -> null
        }
        if(mfgCurrentFragment!=null)
            supportFragmentManager.beginTransaction().replace(R.id.flMainContent, mfgCurrentFragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_piechart -> {
                if(mstrCurrentFragment != FragmentFinancialReport.TAGNAME) {
                    supportFragmentManager.beginTransaction().remove(mfgCurrentFragment).commit()
                    mfgCurrentFragment = FragmentFinancialReport()
                    supportFragmentManager.beginTransaction().replace(R.id.flMainContent, mfgCurrentFragment).commit()
                    mstrCurrentFragment = FragmentFinancialReport.TAGNAME
                }
                return true
                //fragmentManager.beginTransaction().replace(R.id.flMainContent, FragmentFinancialReport()).commit()
            }
            R.id.navigation_expenses -> {
                if(mstrCurrentFragment != FragmentExpensesList.TAGNAME) {
                    supportFragmentManager.beginTransaction().remove(mfgCurrentFragment).commit()
                    mfgCurrentFragment = FragmentExpensesList()
                    supportFragmentManager.beginTransaction().replace(R.id.flMainContent, mfgCurrentFragment).commit()
                    mstrCurrentFragment = FragmentExpensesList.TAGNAME
                }
                return true
                //fragmentManager.beginTransaction().replace(R.id.flMainContent, null).commit()
            }
            R.id.navigation_salary -> {
                if(mstrCurrentFragment != FragmentSalaryList.TAGNAME) {
                    supportFragmentManager.beginTransaction().remove(mfgCurrentFragment).commit()
                    mfgCurrentFragment = FragmentSalaryList()
                    supportFragmentManager.beginTransaction().replace(R.id.flMainContent, mfgCurrentFragment).commit()
                    mstrCurrentFragment = FragmentSalaryList.TAGNAME
                }
                return true
                //fragmentManager.beginTransaction().replace(R.id.flMainContent, null).commit()
            }
        }
        return false
    }

    private fun loadSpinnersContent() {
        mlstSpinnerMonthsList = ArrayList()
        mlstSpinnerMonthsList?.add(resources.getString(R.string.All))

        MonthType.values().forEach { mlstSpinnerMonthsList?.add(it.description(this)) }

        val lstArrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, mlstSpinnerMonthsList)
        lstArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        mspMonthSelector?.adapter = lstArrayAdapter
        mspMonthSelector?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idOfMonth = MonthType.gettingIdFromDescription(applicationContext, mspMonthSelector?.selectedItem.toString())
                StaticCollections.mmtMonthSelected = if(idOfMonth != null) {
                    val monthType = MonthType.fromInt(idOfMonth)
                    monthType
                } else { null }

                SelectionSharedPreferences.insertMonthSelectPreference(applicationContext, StaticCollections.mmtMonthSelected)

                when(mstrCurrentFragment) {
                    FragmentSalaryList.TAGNAME -> {

                    }
                    FragmentExpensesList.TAGNAME -> {
                        (mfgCurrentFragment as FragmentExpensesList).loadExpenseList()
                    }
                    FragmentFinancialReport.TAGNAME -> {
                        (mfgCurrentFragment as FragmentFinancialReport).loadChartData()
                    }
                }
            }
        }

        val strMonthValue = StaticCollections.mmtMonthSelected?.description(this)

        mlstSpinnerMonthsList ?: return
        var nCount = 0
        val nSize = mlstSpinnerMonthsList?.size
        while(nCount < nSize!!) {
            if(mlstSpinnerMonthsList!![nCount]==strMonthValue)
                break
            nCount++
        }
        mspMonthSelector?.setSelection(nCount)
    }
}

