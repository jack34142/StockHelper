package network.co.imge.stockhelper.mvp.model

import MyResponse
import android.util.Log
import network.co.imge.stockhelper.base.BaseModel
import network.co.imge.stockhelper.http.HttpClient
import network.co.imge.stockhelper.mvp.contract.MainContract

class MainModel : BaseModel(), MainContract.IMainModel {
    override fun test(onSuccess: (MyResponse<Any>) -> Unit) {
        disposables?.add(HttpClient.instance.test(onSuccess))
    }
}