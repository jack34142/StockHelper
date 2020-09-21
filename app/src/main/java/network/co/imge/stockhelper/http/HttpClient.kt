package network.co.imge.stockhelper.http

import MyResponse
import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
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
        val client = OkHttpClient.Builder().build()
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.134")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    fun test(onSuccess: (MyResponse<Any>) -> Unit): Disposable {
        return service.test().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = onSuccess,
                onError = onError
            )
    }

}