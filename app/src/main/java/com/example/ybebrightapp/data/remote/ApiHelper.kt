package com.example.ybebrightapp.data.remote

import com.example.ybebrightapp.model.CitiesResponse
import com.example.ybebrightapp.model.CostResponse
import com.example.ybebrightapp.model.ProvinceResponse
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class ApiHelper {

    companion object {
        private const val API_KEY = "b808b359bf96b2b86cf5cf2601299aad"
        private const val URL = "https://api.rajaongkir.com/starter/"

        fun getApiService(): ApiService {

            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("key", API_KEY).build()
                    return@addInterceptor chain.proceed(request)
                }

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    interface ApiService {
        @GET("province")
        fun getProvinces(): Call<ProvinceResponse>

        @GET("city")
        fun getCity(@Query("province") id: String): Call<CitiesResponse>

        @POST("cost")
        fun getCost(@Body request: RequestBody) : Call<CostResponse>
    }
}