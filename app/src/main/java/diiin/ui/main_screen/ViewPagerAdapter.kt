package diiin.ui.main_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(a_fmFragmentManager: FragmentManager, a_lstPages: ArrayList<Fragment>) : FragmentPagerAdapter(a_fmFragmentManager) {

    private val mlstPages: ArrayList<Fragment> = a_lstPages

    override fun getItem(position: Int): Fragment {
        val page = mlstPages[position]
        if (page is RefreshData) {
            page.refresh()
        }
        return page
    }

    override fun getCount(): Int {
        return mlstPages.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}

interface RefreshData {
    fun refresh()
}