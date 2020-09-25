package network.co.imge.stockhelper.ui.activity

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.ui.adapter.viewPager.MainPagerAdapter

class MainActivity : BaseActivity() {
    private val TAG: String = "MainActivity"

    lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
    }

    private fun initView(){
        pager = findViewById(R.id.main_pager)
    }

    private fun initData(){
        pager.adapter = MainPagerAdapter(this)
    }

}