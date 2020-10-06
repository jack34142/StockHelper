package network.co.imge.stockhelper.mvp.model

import android.content.Context
import network.co.imge.stockhelper.base.BaseModel
import network.co.imge.stockhelper.db.NoticeStockDAO
import network.co.imge.stockhelper.mvp.contract.NoticeStockContract
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockModel(val context: Context) : BaseModel(), NoticeStockContract.INoticeStockModel {

    private var noticeStockDAO: NoticeStockDAO? = NoticeStockDAO(context)

    override fun detachView() {
        super.detachView()
        noticeStockDAO?.close()
        noticeStockDAO = null
    }

    override fun addNoticeStock(stock: NoticeStock): NoticeStock {
        return noticeStockDAO!!.insert(stock)
    }

    override fun updateNoticeStock(stock: NoticeStock) {
        noticeStockDAO?.update(stock)
    }

    override fun deleteNoticeStock(id: Long) {
        noticeStockDAO?.delete(id)
    }

    override fun getNoticeStocks(): List<NoticeStock> {
        return noticeStockDAO!!.getAll()
    }

}