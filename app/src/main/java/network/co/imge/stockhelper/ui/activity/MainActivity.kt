package network.co.imge.stockhelper.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.ui.adapter.viewPager.MainPagerAdapter
import java.util.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    private val TAG: String = "MainActivity"

    lateinit var pager: ViewPager2
    var backTime: Long = 0

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

    override fun onBackPressed() {
//        super.onBackPressed()
        val now = Date().time

        if (now - backTime < 2000) {
            exitProcess(0)
        }else{
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
            backTime = now
        }
    }

}