package network.co.imge.stockhelper.http

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("https://mis.twse.com.tw/stock/api/getStockInfo.jsp")
    fun getNowPrice(@Query("ex_ch") q: String): Observable<ResponseBody>
}