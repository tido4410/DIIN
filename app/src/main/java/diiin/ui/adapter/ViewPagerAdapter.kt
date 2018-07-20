package diiin.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(a_fmFragmentManager : FragmentManager, a_lstPages : ArrayList<Fragment>) : FragmentPagerAdapter(a_fmFragmentManager) {

    private val mlstPages : ArrayList<Fragment> = a_lstPages

    override fun getItem(position: Int): Fragment {
        return mlstPages[position]
    }

    override fun getCount(): Int {
        return mlstPages.size
    }

}