package com.example.ybebrightapp.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ybebrightapp.data.local.LocalDataSource
import com.example.ybebrightapp.data.remote.RemoteDataSource
import com.example.ybebrightapp.ingredients.Ingredient
import com.example.ybebrightapp.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository (
    private val remote: RemoteDataSource,
    private val local: LocalDataSource): DataSource {

    companion object {
        @Volatile
        private var INSTANCE: MainRepository? = null

        fun getInstance(remote: RemoteDataSource, local: LocalDataSource): MainRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MainRepository(remote, local).apply { INSTANCE = this }
        }
    }

    override fun getProduct(): List<Product> = local.getProducts()

    override fun getNews() {
        TODO("Not yet implemented")
    }

    override fun getAllList(): List<Agent> = local.getListAuto()

    override fun getListAgentTunggal(nik: String?, poin: Int): Agent? = local.getListAgentTunggal(nik, poin)

    override fun getListAgent(nik: String?, poin: Int): Agent? = local.getListAgent(nik, poin)

    override fun getListReseller(nik: String?, poin: Int): Agent? = local.getListReseller(nik, poin)

    override fun getPoin(poin: Int): List<Poin> = local.getPoin(poin)

    override fun getIngredients(): List<Ingredient> = local.getIngredients()

    override fun getProvince(): LiveData<List<Province>> {
        val client = remote.getProvince()
        val list = MutableLiveData<List<Province>>()
        client.enqueue(object : Callback<ProvinceResponse> {
            override fun onResponse(call: Call<ProvinceResponse>, response: Response<ProvinceResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main?.data!!
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return list
    }

    override fun getCity(id: String): LiveData<List<City>> {
        val client = remote.getCity(id)
        val list = MutableLiveData<List<City>>()
        client.enqueue(object : Callback<CitiesResponse> {
            override fun onResponse(call: Call<CitiesResponse>, response: Response<CitiesResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main?.data!!
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CitiesResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return list
    }

    override fun getCost(request: okhttp3.RequestBody): LiveData<List<Costs>> {
        val client = remote.getCost(request)
        val list = MutableLiveData<List<Costs>>()
        client.enqueue(object : Callback<CostResponse> {
            override fun onResponse(call: Call<CostResponse>, response: Response<CostResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main?.list?.get(0)?.costs
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CostResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return list
    }
}