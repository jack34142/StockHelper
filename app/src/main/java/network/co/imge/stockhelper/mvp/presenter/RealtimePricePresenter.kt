package network.co.imge.stockhelper.mvp.presenter

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.mvp.model.RealtimePriceModel

class RealtimePricePresenter: BasePresenter<RealtimePriceContract.IRealtimePriceView>(), RealtimePriceContract.IRealtimePricePresenter {
    private var mvpModel: RealtimePriceModel? = null

    override fun attachView(mvpView: RealtimePriceContract.IRealtimePriceView) {
        super.attachView(mvpView)
        mvpModel = RealtimePriceModel((mvpView as Fragment).context!!)
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }

    override fun getRealtimePrice() {
        mvpModel!!.getRealtimePrice(){
            mvpView?.getRealtimePriceCallback(it.toMutableList())
        }.let {
            disposables?.add(it)
        }
    }
}