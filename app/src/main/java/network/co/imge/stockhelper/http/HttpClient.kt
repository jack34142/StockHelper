package network.co.imge.stockhelper.http

import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import network.co.imge.stockhelper.pojo.MyResponse
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

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
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager{
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val logging = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                var len = message.length
                if (len > 8192) len = 8192
                Log.d(TAG, message.substring(0, len))
            }
        })
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(logging)
            .sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(object : HostnameVerifier{
                override fun verify(host: String?, session: SSLSession?): Boolean {
                    val dns = listOf(
                        "mis.twse.com.tw",
                        "smart.tdcc.com.tw"
                    )
                    return dns.contains(host)
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
        val client = builder.build()

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.google.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    fun getRealtimePrice(query: String, onResponse: (MyResponse<List<TwseResponse>>) -> Unit): Disposable {
        return service.getNowPrice(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .repeatWhen{ it.delay(5, TimeUnit.SECONDS) }
            .retryWhen{ it.delay(5, TimeUnit.SECONDS) }
            .subscribeBy(
                onNext = {
                    val datas = mutableListOf<TwseResponse>()

                    val json = JSONObject(it.string())
                    val rtmessage = json.getString("rtmessage")

                    if(json.getString("rtcode") == "0000"){
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
                                openPrice = if(msg.has("o")) msg.getDouble("o") else null,
                                highPrice = if(msg.has("h")) msg.getDouble("h") else null,
                                lowPrice = if(msg.has("l")) msg.getDouble("l") else null,
                                yesterdayPrice = msg.getDouble("y"),
                                nowPrice = msg.getString("z").toDoubleOrNull(),
                                nowQty = msg.getString("s").toIntOrNull(),
                                totalQty = msg.getInt("v"),
                                type = msg.getString("ex")
                            )
                            datas.add(twseResponse)
                        }
                        onResponse(MyResponse(1, rtmessage, datas))
                    }else{
                        onResponse(MyResponse(-1, rtmessage))
                    }
                },
                onError = onError
            )
    }

    fun getTaiex(onResponse: (MyResponse<TaiexBean>) -> Unit): Disposable{
        return service.getTaiex(Date().time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .repeatWhen{ it.delay(5, TimeUnit.SECONDS) }
            .retryWhen{ it.delay(5, TimeUnit.SECONDS) }
            .subscribeBy(
                onNext = {
                    val json = JSONObject(it.string())
                    val rtmessage = json.getString("rtmessage")

                    if(json.getString("rtcode") == "0000"){
                        val infoArray = json.getJSONArray("infoArray").getJSONObject(0)
                        val staticObj = json.getJSONObject("staticObj")
                        val taiex = TaiexBean(
                            infoArray.getString("n"),
                            infoArray.getDouble("z"),
                            infoArray.getDouble("o"),
                            infoArray.getDouble("h"),
                            infoArray.getDouble("l"),
                            infoArray.getDouble("y"),
                            infoArray.getInt("v")/100.0,
                            staticObj.getInt("tv")
                        )
                        onResponse(MyResponse(1, rtmessage, taiex))
                    }else{
                        onResponse(MyResponse(-1, rtmessage))
                    }
                },
                onError = onError
            )
    }

    fun getStockType(onResponse: (MyResponse<Map<String, String>>) -> Unit): Disposable{
        return service.getStockType()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .repeatWhen{ it.delay(5, TimeUnit.SECONDS) }
//            .retryWhen{ it.delay(5, TimeUnit.SECONDS) }
            .subscribeBy(
                onNext = {
                    val typeMap = mutableMapOf<String, String>()
                    val rows = it.string().split("\n")
                    val len_rows = rows.size

                    for (i in 1 until len_rows){
                        val row = rows[i]
                        val data = row.split(",")

                        try {
                            val type = data[3].trim()
                            if (type.isNotEmpty()){
                                val stockName = "${data[1]} ${data[2]}"
                                when (type){
                                    "上市" -> typeMap[stockName] = "tse"
                                    "上櫃" -> typeMap[stockName] = "otc"
                                }
                            }
                        }catch (e: IndexOutOfBoundsException){
                            Log.e(TAG, "IndexOutOfBoundsException => getStockType csv readline\nline=$i, string = $row")
                        }
                    }
                    onResponse(MyResponse(1, "success", typeMap))
                },
                onError = {
                    onResponse(MyResponse(-1, it.toString()))
                    onError(it)
                }
            )
    }

}