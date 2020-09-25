package network.co.imge.stockhelper.ui.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import network.co.imge.stockhelper.ui.fragment.NoticeStockFragment
import network.co.imge.stockhelper.ui.fragment.RealtimePriceFragment

class MainPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragments: List<Fragment> = listOf(
        NoticeStockFragment(),
        RealtimePriceFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}