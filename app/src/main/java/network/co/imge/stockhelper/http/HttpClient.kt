package network.co.imge.stockhelper.http

import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import network.co.imge.stockhelper.data.MyData
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TwseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
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

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(object : HostnameVerifier{
                override fun verify(host: String?, session: SSLSession?): Boolean {
                    val dns = listOf(
                        "mis.twse.com.tw"
                    )
                    return dns.contains(host)
                }
            })
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

    private fun getSSLFactory(): SSLSocketFactory {
        //證書忽略添加下面代碼（1）打開即可
//         Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.socketFactory
    }

    fun getRealtimePrice(stocks: List<NoticeStock>, onSuccess: (List<TwseResponse>) -> Unit): Disposable {
        val q = stocks.map {
            it.type + "_" + it.stockId + ".tw"
        }.toSet().joinToString(separator = "|")

        return service.getNowPrice(q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .repeatWhen{ it.delay(5, TimeUnit.SECONDS) }
            .retryWhen{ it.delay(5, TimeUnit.SECONDS) }
            .subscribeBy(
                onNext = {
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
                            nowPrice = msg.getString("z").toDoubleOrNull(),
                            nowQty = msg.getString("s").toIntOrNull(),
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