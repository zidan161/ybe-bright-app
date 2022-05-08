package com.zidan.ybebrightapp.data.remote

import com.zidan.ybebrightapp.model.CitiesResponse
import com.zidan.ybebrightapp.model.CostResponse
import com.zidan.ybebrightapp.model.ProvinceResponse
import retrofit2.Call

class RemoteDataSource {

    private val apiHelper = ApiHelper

    fun getProvince(): Call<ProvinceResponse> = apiHelper.getApiService().getProvinces()

    fun getCity(id: String): Call<CitiesResponse> = apiHelper.getApiService().getCity(id)

    fun getAllCity(): Call<CitiesResponse> = apiHelper.getApiService().getAllCity()

    fun getCost(origin: String, cityId: String, weight: Int, courier: String): Call<CostResponse> =
        apiHelper.getApiService().getCost(origin, cityId, weight, courier)
}