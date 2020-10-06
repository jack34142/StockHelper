package network.co.imge.stockhelper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import network.co.imge.stockhelper.http.HttpClient

class DbHelper(context:Context): SQLiteOpenHelper(context, "my.db", null, 5) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(NoticeStockDAO.getCreateString())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 5){
            db?.execSQL("DROP TABLE IF EXISTS " + NoticeStockDAO.TABLE_NAME)
            db?.execSQL(NoticeStockDAO.getCreateString())
        }
    }
}