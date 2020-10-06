package network.co.imge.stockhelper.mvp.presenter

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.data.MyData
import network.co.imge.stockhelper.mvp.contract.NoticeStockContract
import network.co.imge.stockhelper.mvp.model.NoticeStockModel
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockPresenter: BasePresenter<NoticeStockContract.INoticeStockView>(), NoticeStockContract.INoticeStockPresenter {
    private val TAG: String = "MainPresenter"

    private var mvpModel: NoticeStockModel? = null

    override fun attachView(mvpView: NoticeStockContract.INoticeStockView) {
        super.attachView(mvpView)
        mvpModel = NoticeStockModel((mvpView as Fragment).context!!)
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }

    override val addNoticeStock: (NoticeStock) -> Unit = {
        mvpModel?.addNoticeStock(it)
    }

    override val updateNoticeStock: (NoticeStock) -> Unit = {
        mvpModel?.updateNoticeStock(it)
    }

    override val deleteNoticeStock: (Long) -> Unit = {
        mvpModel?.deleteNoticeStock(it)
    }

    override fun getNoticeStocks() {
        mvpModel!!.getNoticeStocks().let {
            MyData.noticeStocks = it.toMutableList()
            mvpView?.getNoticeStocksCallback()
        }
    }
}