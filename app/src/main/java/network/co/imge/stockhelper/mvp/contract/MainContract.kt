package network.co.imge.stockhelper.mvp.contract

import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TwseResponse

class MainContract {
    interface IMainView: IView{
        fun getNoticeStocksCallback(stocks: MutableList<NoticeStock>)
    }

    interface IMainPresenter: IPresenter<IMainView>{
        val addNoticeStock: (NoticeStock) -> Unit
        val updateNoticeStock: (NoticeStock) -> Unit
        val deleteNoticeStock: (Long) -> Unit
        fun getRealtimePrice(stocks: List<NoticeStock>)
        fun getNoticeStocks()
    }

    interface IMainModel: IModel {
        fun addNoticeStock(stock: NoticeStock): NoticeStock
        fun updateNoticeStock(stock: NoticeStock)
        fun deleteNoticeStock(id: Long)
        fun getRealtimePrice(stocks: List<NoticeStock>, onSuccess: (List<TwseResponse>) -> Unit): Disposable
        fun getNoticeStocks(): List<NoticeStock>
    }
}