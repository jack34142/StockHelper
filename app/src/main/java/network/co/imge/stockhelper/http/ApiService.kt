package network.co.imge.stockhelper.http

import io.reactivex.Single
import network.co.imge.stockhelper.pojo.TwseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("https://mis.twse.com.tw/stock/api/getStockInfo.jsp")
    fun getNowPrice(@Query("ex_ch") q: String): Single<TwseResponse>
}