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
        fun getNowPrice()
        fun addNoticeStock(stock: NoticeStock)
        fun getNoticeStocks()
    }

    interface IMainModel: IModel {
        fun getNowPrice(onSuccess: (TwseResponse) -> Unit): Disposable
        fun addNoticeStock(stock: NoticeStock)
        fun getNoticeStocks(): List<NoticeStock>
    }
}