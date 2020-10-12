package network.co.imge.stockhelper.mvp.model

import android.content.Context
import io.reactivex.disposables.Disposable
import network.co.imge.stockhelper.base.BaseModel
import network.co.imge.stockhelper.data.MyData
import network.co.imge.stockhelper.http.HttpClient
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.pojo.MyResponse
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse

class RealtimePriceModel(val context: Context) : BaseModel(), RealtimePriceContract.IRealtimePriceModel {

    override fun getRealtimePrice(onResponse: (MyResponse<List<TwseResponse>>) -> Unit): Disposable {
        return HttpClient.instance.getRealtimePrice(MyData.noticeStocks, onResponse)
    }

    override fun getTaiex(onResponse: (MyResponse<TaiexBean>) -> Unit): Disposable {
        return HttpClient.instance.getTaiex(onResponse)
    }
}