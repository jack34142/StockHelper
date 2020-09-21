package network.co.imge.stockhelper.http

import MyResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    fun test(): Single<MyResponse<Any>>
}