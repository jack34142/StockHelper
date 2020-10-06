package network.co.imge.stockhelper.mvp.contract

import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockContract {
    interface INoticeStockView: IView{
        fun getNoticeStocksCallback()
    }

    interface INoticeStockPresenter: IPresenter<INoticeStockView>{
        val addNoticeStock: (NoticeStock) -> Unit
        val updateNoticeStock: (NoticeStock) -> Unit
        val deleteNoticeStock: (Long) -> Unit
        fun getNoticeStocks()
    }

    interface INoticeStockModel: IModel {
        fun addNoticeStock(stock: NoticeStock): NoticeStock
        fun updateNoticeStock(stock: NoticeStock)
        fun deleteNoticeStock(id: Long)
        fun getNoticeStocks(): List<NoticeStock>
    }
}