package momonyan.contactlensmanage

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import momonyan.contactlensmanage.fragment.TabMain
import momonyan.contactlensmanage.fragment.TabMore

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val setFragment: Fragment
        when (position) {
            0 -> setFragment = TabMain()
            1 -> setFragment = TabMore()
            else -> error("TabError")
        }
        return setFragment
    }

    override fun getCount(): Int {
        return 2
    }
}