package network.co.imge.stockhelper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import network.co.imge.stockhelper.http.HttpClient

class DbHelper(context:Context): SQLiteOpenHelper(context, "my.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(NoticeStockDAO.getCreateString())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}