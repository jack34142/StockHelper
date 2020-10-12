package network.co.imge.stockhelper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import network.co.imge.stockhelper.db.NoticeStockDAO
import network.co.imge.stockhelper.pojo.NoticeStock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SQLiteTest {
    private var noticeStockDAO: NoticeStockDAO? = null

    private var latestId: Long? = 0

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        noticeStockDAO = NoticeStockDAO(context)
    }

    @After
    fun finish() {
        noticeStockDAO?.close()
    }

    @Test
    fun testPreConditions() {
        assertNotNull(noticeStockDAO)
    }

    @Test
    @Throws(Exception::class)
    fun testInsert() {
        val stockId = "2330"
        val type = "tse"
        val priceFrom = 450.0
        val priceTo = 500.0

        noticeStockDAO?.insert(NoticeStock(
            stockId = stockId,
            type = type,
            priceFrom = priceFrom,
            priceTo = priceTo
        )).run {
            latestId = this?.id
        }

        val noticeStocks = noticeStockDAO?.getAll()!!
        assertEquals(noticeStocks.size, 1)

        val noticeStock = noticeStocks[0]
        assertEquals(noticeStock.stockId, stockId)
        assertEquals(noticeStock.type, type)
        assertEquals(noticeStock.priceFrom, priceFrom)
        assertEquals(noticeStock.priceTo, priceTo)
    }

    @Test
    fun testUpdate() {
        val stockId = "2330"
        val type = "tse"
        val priceFrom = 400.0
        val priceTo = 550.0

        noticeStockDAO?.update(NoticeStock(
            id = 0,
            stockId = stockId,
            type = type,
            priceFrom = priceFrom,
            priceTo = priceTo
        ))

        val noticeStocks = noticeStockDAO?.getAll()!!
        assertEquals(noticeStocks.size, 1)

        val noticeStock = noticeStocks[0]
        assertEquals(noticeStock.stockId, stockId)
        assertEquals(noticeStock.type, type)
        assertEquals(noticeStock.priceFrom, priceFrom)
        assertEquals(noticeStock.priceTo, priceTo)
    }

    @Test
    fun testDelete() {
        noticeStockDAO?.delete(0)
        val noticeStocks = noticeStockDAO?.getAll()!!
        assertEquals(noticeStocks.size, 0)
    }
}