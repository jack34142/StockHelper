package network.co.imge.stockhelper.mvp.contract

import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView
import network.co.imge.stockhelper.pojo.MyResponse
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse

class MainContract {
    interface IMainView: IView{
        fun getNoticeStockCallback(stocks: MutableMap<String, NoticeStock>)
        fun getNoticeStock(): MutableMap<String, NoticeStock>
        fun getRealtimePriceCallback(datas: MutableList<TwseResponse>, goals: MutableList<TwseResponse>)
        fun getTaiexCallback(taiexbean: TaiexBean?)
        fun stockGoal(msg: String)
    }

    interface IMainPresenter: IPresenter<IMainView>{
        fun addNoticeStock(noticeStock: NoticeStock)
        fun updateNoticeStock(noticeStock: NoticeStock)
        fun deleteNoticeStock(stockId: String): Boolean
        fun getNoticeStocks()
        fun getRealtimePrice()
        fun getTaiex()
        fun getStockType(onSuccess: (Map<String, String>) -> Unit)
    }

    interface IMainModel: IModel {
        fun addNoticeStock(stock: NoticeStock): NoticeStock
        fun updateNoticeStock(stock: NoticeStock)
        fun deleteNoticeStock(id: Long)
        fun getNoticeStocks(): MutableMap<String, NoticeStock>
        fun getRealtimePrice(query: String, onResponse: (MyResponse<List<TwseResponse>>) -> Unit): Disposable
        fun getTaiex(onResponse: (MyResponse<TaiexBean>) -> Unit): Disposable
        fun getStockType(onResponse: (MyResponse<Map<String, String>>) -> Unit): Disposable
    }
}