package network.co.imge.stockhelper.mvp.presenter

import android.content.Context
import android.util.Log
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.model.MainModel
import network.co.imge.stockhelper.pojo.NoticeStock

class MainPresenter: BasePresenter<MainContract.IMainView>(), MainContract.IMainPresenter {
    private val TAG: String = "MainPresenter"

    private var mvpModel: MainModel? = null

    override fun attachView(mvpView: MainContract.IMainView) {
        super.attachView(mvpView)
        mvpModel = MainModel(mvpView as Context)
    }

    override fun getNowPrice() {
        mvpModel!!.getNowPrice {

        }.let {
            disposables?.add(it)
        }
    }

    override fun addNoticeStock(stock: NoticeStock) {
        mvpModel!!.addNoticeStock(stock)
    }

    override fun getNoticeStocks() {
        mvpModel!!.getNoticeStocks().let {
            mvpView?.getNoticeStocksCallback(it.toMutableList())
        }
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }
}