package network.co.imge.stockhelper.http

import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import network.co.imge.stockhelper.pojo.TwseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

    fun getNowPrice(onSuccess: (TwseResponse) -> Unit): Disposable {
        return service.getNowPrice("tse_2330.tw").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = onSuccess,
                onError = onError
            )
    }

}