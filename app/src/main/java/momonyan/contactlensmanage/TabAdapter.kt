package momonyan.contactlensmanage

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import momonyan.contactlensmanage.Fragment.Tab1

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val setFragment: Fragment
        when (position) {
            0 -> setFragment = Tab1()
            1 -> setFragment = Tab1() //しない
            else -> error("TabError")
        }
        return setFragment
    }

    override fun getCount(): Int {
        return 2
    }
}