package diiin.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import diiin.ui.fragments.FragmentExpensesList
import diiin.ui.fragments.FragmentFinancialReport
import diiin.ui.fragments.FragmentSalaryList

class ViewPagerAdapter(a_fmFragmentManager : FragmentManager) : FragmentPagerAdapter(a_fmFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> FragmentExpensesList()
            1 -> FragmentFinancialReport()
            2 -> FragmentSalaryList()
            else -> FragmentExpensesList()
        }
    }

    override fun getCount(): Int {
        return 3
    }

}