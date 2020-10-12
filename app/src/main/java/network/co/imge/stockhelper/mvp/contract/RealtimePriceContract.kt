package network.co.imge.stockhelper.mvp.contract

import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView
import network.co.imge.stockhelper.pojo.MyResponse
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse

class RealtimePriceContract {
    interface IRealtimePriceView: IView{
        fun getRealtimePriceCallback(datas: MutableList<TwseResponse>, goals: MutableList<TwseResponse>)
        fun getTaiexCallback(taiexbean: TaiexBean?)
        fun stockGoal(msg: String)
    }

    interface IRealtimePricePresenter: IPresenter<IRealtimePriceView>{
        fun getRealtimePrice()
        fun getTaiex()
    }

    interface IRealtimePriceModel: IModel {
        fun getRealtimePrice(onResponse: (MyResponse<List<TwseResponse>>) -> Unit): Disposable
        fun getTaiex(onResponse: (MyResponse<TaiexBean>) -> Unit): Disposable
    }
}