package network.co.imge.stockhelper.mvp.contract

import MyResponse
import network.co.imge.stockhelper.base.IModel
import network.co.imge.stockhelper.base.IPresenter
import network.co.imge.stockhelper.base.IView

class MainContract {
    interface IMainView: IView{

    }

    interface IMainPresenter: IPresenter<IMainView>{
        fun test()
    }

    interface IMainModel: IModel {
        fun test(onSuccess: (MyResponse<Any>) -> Unit)
    }
}