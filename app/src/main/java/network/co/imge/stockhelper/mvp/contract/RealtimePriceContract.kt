package network.co.imge.stockhelper.mvp.contract

import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView
import network.co.imge.stockhelper.pojo.TwseResponse

class RealtimePriceContract {
    interface IRealtimePriceView: IView{
        fun getRealtimePriceCallback(datas: MutableList<TwseResponse>)
        fun stockGoal(msg: String)
    }

    interface IRealtimePricePresenter: IPresenter<IRealtimePriceView>{
        fun getRealtimePrice()
    }

    interface IRealtimePriceModel: IModel {
        fun getRealtimePrice(onSuccess: (List<TwseResponse>) -> Unit): Disposable
    }
}