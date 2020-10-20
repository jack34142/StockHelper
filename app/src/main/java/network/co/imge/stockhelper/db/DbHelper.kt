package network.co.imge.stockhelper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context:Context): SQLiteOpenHelper(context, "my.db", null, 6) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(NoticeStockDAO.getCreateString())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 6){
            db?.execSQL("DROP TABLE IF EXISTS " + NoticeStockDAO.TABLE_NAME)
            db?.execSQL(NoticeStockDAO.getCreateString())
        }
    }
}