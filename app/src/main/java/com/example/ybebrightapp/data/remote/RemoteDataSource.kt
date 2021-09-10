package com.example.ybebrightapp.data.remote

import com.example.ybebrightapp.model.CitiesResponse
import com.example.ybebrightapp.model.CostResponse
import com.example.ybebrightapp.model.ProvinceResponse
import okhttp3.RequestBody
import retrofit2.Call

class RemoteDataSource {

    fun getProvince(): Call<ProvinceResponse> = ApiHelper.getApiService().getProvinces()

    fun getCity(id: String): Call<CitiesResponse> = ApiHelper.getApiService().getCity(id)

    fun getCost(request: RequestBody): Call<CostResponse> =
        ApiHelper.getApiService().getCost(request)
}