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
        val COL_PRICE_FROM = "price_from"
        val COL_PRICE_TO = "price_to"

        fun getCreateString(): String{
            return "CREATE TABLE if not exists $TABLE_NAME(" +
                    "$PRIMARY_KEY integer PRIMARY KEY autoincrement," +
                    "$COL_STOCK_ID text, " +
                    "$COL_TYPE text, " +
                    "$COL_PRICE_FROM REAL, " +
                    "$COL_PRICE_TO REAL)"
        }
    }

    fun insert(stock: NoticeStock): NoticeStock{
        val values = ContentValues().apply {
            put(COL_STOCK_ID, stock.stockId)
            put(COL_TYPE, stock.type)
            put(COL_PRICE_FROM, stock.priceFrom)
            put(COL_PRICE_TO, stock.priceTo)
        }
        val id = db!!.insert(TABLE_NAME, null, values)
        stock.id = id
        return stock
    }

    fun update(stock: NoticeStock){
        val values = ContentValues().apply {
            put(COL_STOCK_ID, stock.stockId)
            put(COL_TYPE, stock.type)
            put(COL_PRICE_FROM, stock.priceFrom)
            put(COL_PRICE_TO, stock.priceTo)
        }
        db?.update(TABLE_NAME, values,"$PRIMARY_KEY = ?", arrayOf(stock.id.toString()))
    }

    fun delete(id: Long){
        db?.delete(TABLE_NAME, "$PRIMARY_KEY = ?", arrayOf(id.toString()))
    }

    fun getAll(): List<NoticeStock>{
        return db!!.query(TABLE_NAME, null, null, null, null, null, null).run {
            mutableListOf<NoticeStock>().apply {
                while (moveToNext()){
                    add(NoticeStock(
                        getLong(getColumnIndex(PRIMARY_KEY)),
                        getString(getColumnIndex(COL_STOCK_ID)),
                        getString(getColumnIndex(COL_TYPE)),
                        getDouble(getColumnIndex(COL_PRICE_FROM)),
                        getDouble(getColumnIndex(COL_PRICE_TO))
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