package network.co.imge.stockhelper.http

import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TwseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier

class HttpClient private constructor(){
    private val TAG: String = "HttpClient"

    private val service: ApiService

    private val onError: (e: Throwable) -> Unit = {
        it.printStackTrace()
    }

    companion object {
        val instance: HttpClient by lazy { HttpClient() }
    }

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .hostnameVerifier(HostnameVerifier { url, sslSession ->
                val trusts = listOf<String>("mis.twse.com.tw")
                return@HostnameVerifier trusts.contains(url)
            })
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.google.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    fun getRealtimePrice(stocks: List<NoticeStock>, onSuccess: (List<TwseResponse>) -> Unit): Disposable {
        val q = stocks.map {
            it.type + "_" + it.stockId + ".tw"
        }.joinToString(separator = "|")

        return service.getNowPrice(q).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val datas = mutableListOf<TwseResponse>()

                    val json = JSONObject(it.string())
                    val msgArray = json.getJSONArray("msgArray")

                    val len = msgArray.length()
                    for (i in 0 until len){
                        val msg = msgArray.getJSONObject(i)
                        val twseResponse = TwseResponse(
                            stockId = msg.getString("c"),
                            stockName = msg.getString("n"),
                            best5purchaseQty = msg.getString("g").split("_").map { it.toIntOrNull() }.filterNotNull(),
                            best5purchasePrice = msg.getString("b").split("_").map { it.toDoubleOrNull() }.filterNotNull(),
                            best5sellPrice = msg.getString("a").split("_").map { it.toDoubleOrNull() }.filterNotNull(),
                            best5sellQty = msg.getString("f").split("_").map { it.toIntOrNull() }.filterNotNull(),
                            openPrice = msg.getDouble("o"),
                            highPrice = msg.getDouble("h"),
                            lowPrice = msg.getDouble("l"),
                            yesterdayPrice = msg.getDouble("y"),
                            nowPrice = msg.getDouble("z"),
                            nowQty = msg.getInt("s"),
                            totalQty = msg.getInt("v"),
                            type = msg.getString("ex")
                        )
                        datas.add(twseResponse)
                    }
                    onSuccess(datas)
                },
                onError = onError
            )
    }

}