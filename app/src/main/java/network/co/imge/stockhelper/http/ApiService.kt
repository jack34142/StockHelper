package network.co.imge.stockhelper.http

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("https://mis.twse.com.tw/stock/api/getStockInfo.jsp")
    fun getNowPrice(@Query("ex_ch") q: String): Observable<ResponseBody>

    @GET("https://mis.twse.com.tw/stock/data/mis_ohlc_TSE.txt")
    fun getTaiex(@Query("_") timestamp: Long): Observable<ResponseBody>

    @GET("http://mopsfin.twse.com.tw/opendata/t187ap03_L.csv")
    fun getTseList(): Observable<ResponseBody>
}