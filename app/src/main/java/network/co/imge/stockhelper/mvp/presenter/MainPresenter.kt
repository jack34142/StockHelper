package network.co.imge.stockhelper.mvp.presenter

import android.util.Log
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.model.MainModel

class MainPresenter: BasePresenter<MainContract.IMainView>(), MainContract.IMainPresenter {
    private val TAG: String = "MainPresenter"

    private var mvpModel: MainModel? = MainModel()

    override fun test() {
        mvpModel?.test {
            Log.d(TAG, it.code.toString())
            Log.d(TAG, it.msg)
        }
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }
}