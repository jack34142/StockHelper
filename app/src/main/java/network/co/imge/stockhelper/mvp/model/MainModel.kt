package network.co.imge.stockhelper.mvp.model

import android.content.Context
import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.BaseModel
import network.co.imge.stockhelper.db.NoticeStockDAO
import network.co.imge.stockhelper.http.HttpClient
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TwseResponse

class MainModel(val context: Context) : BaseModel(), MainContract.IMainModel {

    private var noticeStockDAO: NoticeStockDAO? = NoticeStockDAO(context)

    override fun detachView() {
        super.detachView()
        noticeStockDAO?.close()
        noticeStockDAO = null
    }

    override fun getNowPrice(onSuccess: (TwseResponse) -> Unit): Disposable {
        return HttpClient.instance.getNowPrice(onSuccess)
    }

    override fun addNoticeStock(stock: NoticeStock) {
        noticeStockDAO?.insert(stock)
    }

    override fun getNoticeStocks(): List<NoticeStock> {
        return noticeStockDAO!!.getAll()
    }

}