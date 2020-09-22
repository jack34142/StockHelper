package network.co.imge.stockhelper.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockDAO(context: Context) {
    private val TAG: String = "NoticeStockDAO"

    private var db: SQLiteDatabase? = DbHelper(context).writableDatabase

    companion object {
        val TABLE_NAME = "notice_stock"

        val PRIMARY_KEY = "id"
        val COL_STOCK_ID = "stock_id"
        val COL_TYPE = "type"
        val COL_PRICE = "price"

        fun getCreateString(): String{
            return "CREATE TABLE if not exists $TABLE_NAME(" +
                    "$PRIMARY_KEY integer PRIMARY KEY autoincrement," +
                    "$COL_STOCK_ID text, " +
                    "$COL_TYPE text, " +
                    "$COL_PRICE REAL)"
        }
    }

    fun insert(stock: NoticeStock){
        val stockId = stock.stockId
        val values = ContentValues().apply {
            put(COL_STOCK_ID, stock.stockId)
            put(COL_TYPE, stock.type)
            put(COL_PRICE, stock.price)
        }
        val count = db!!.update(TABLE_NAME, values,
            "$COL_STOCK_ID = ?", arrayOf(stockId))
        if (count == 0){
            db?.insert(TABLE_NAME, null, values)
        }
    }

    fun getAll(): List<NoticeStock>{
        return db!!.query(TABLE_NAME, null, null, null, null, null, null).run {
            mutableListOf<NoticeStock>().apply {
                while (moveToNext()){
                    add(NoticeStock(
                        getString(getColumnIndex(COL_STOCK_ID)),
                        getString(getColumnIndex(COL_TYPE)),
                        getDouble(getColumnIndex(COL_PRICE))
                    ))
                }
            }
        }
    }

    fun close(){
        db?.close()
        db = null
    }
}