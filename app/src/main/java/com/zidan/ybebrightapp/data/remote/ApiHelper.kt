package com.zidan.ybebrightapp.data.remote

import com.zidan.ybebrightapp.model.CitiesResponse
import com.zidan.ybebrightapp.model.CostResponse
import com.zidan.ybebrightapp.model.ProvinceResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

        @GET("city")
        fun getAllCity(): Call<CitiesResponse>

        @FormUrlEncoded
        @POST("cost")
        fun getCost(
            @Field("origin") origin: String,
            @Field("destination") destination: String,
            @Field("weight") weight: Int,
            @Field("courier") courier: String,
        ) : Call<CostResponse>
    }
}