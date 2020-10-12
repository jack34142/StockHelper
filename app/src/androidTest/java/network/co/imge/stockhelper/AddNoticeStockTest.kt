package network.co.imge.stockhelper

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import network.co.imge.stockhelper.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test

class AddNoticeStockTest {
    @Rule
    @JvmField
    var mainActivity = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addTest(){
        // 點擊新增
        onView(withId(R.id.noticeStock_add)).perform(click())
        // 輸入股號
        onView(withId(R.id.addNoticeStock_stockId)).perform(typeText("2330"), closeSoftKeyboard())
        // 選擇上市
        onView(withId(R.id.addNoticeStock_tse)).perform(click())
        // 設定提醒價 範圍 min
        onView(withId(R.id.addNoticeStock_priceFrom)).perform(typeText("450"), closeSoftKeyboard())
        // 設定提醒價 範圍 max
        onView(withId(R.id.addNoticeStock_priceTo)).perform(typeText("500"), closeSoftKeyboard())
    }
}