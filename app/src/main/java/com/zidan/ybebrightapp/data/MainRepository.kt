package com.zidan.ybebrightapp.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.ybebrightapp.data.local.LocalDataSource
import com.zidan.ybebrightapp.data.remote.RemoteDataSource
import com.zidan.ybebrightapp.howto.HowTo
import com.zidan.ybebrightapp.ingredients.Ingredient
import com.zidan.ybebrightapp.model.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zidan.ybebrightapp.model.Agent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository (private val remote: RemoteDataSource, private val local: LocalDataSource):
    DataSource {

    companion object {
        @Volatile
        private var INSTANCE: MainRepository? = null

        fun getInstance(remote: RemoteDataSource, local: LocalDataSource): MainRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MainRepository(remote, local).apply { INSTANCE = this }
        }
    }

    private val mDatabase = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun getProduct(): List<Product> = local.getProducts()

    override fun getAllMember(): MutableLiveData<MutableList<Agent>?> {

        val allAgents = MutableLiveData<MutableList<Agent>?>()
        allAgents.value = null
        mDatabase.getReference("member").get().addOnSuccessListener {
            val root = it.children
            val list = mutableListOf<Agent>()
            for (place in root) {
                for (agent in place.children) {
                    val member = agent.getValue(Agent::class.java)!!
                    list.add(member)
                }
            }
            allAgents.value = list
        }
        return allAgents
    }

    override fun getMemberByValue(id: String?): MutableLiveData<MutableList<Cabang>?> {

        val allAgents = MutableLiveData<MutableList<Cabang>?>()
        allAgents.value = null
        mDatabase.getReference("member").get().addOnSuccessListener {
            val root = it.children
            val allList = mutableListOf<Cabang>()
            main@ for (place in root) {
                for (agent in place.children) {
                    if (id != null) {
                        if (agent.key == id) {
                            val member = agent.getValue(Agent::class.java)
                            val ref = Cabang(place.key!!, listOf(member!!))
                            allList.add(ref)
                            break@main
                        }
                    } else {
                        val member = agent.getValue(Agent::class.java)
                        val ref = Cabang(place.key!!, listOf(member!!))
                        allList.add(ref)
                    }
                }
            }
            allAgents.value = allList
        }
        return allAgents
    }

    override fun getMember(id: String): MutableLiveData<Agent?> {

        val fixedData = MutableLiveData<Agent?>()
        fixedData.value = null
        mDatabase.getReference("member").get().addOnSuccessListener {
            val root = it.children

            main@ for (place in root) {
                for (agent in place.children) {
                    if (agent.key == id) {
                        fixedData.value = agent.getValue(Agent::class.java)
                        break@main
                    }
                }
            }
        }
        return fixedData
    }

    override fun getPoin(poin: Int): List<Poin> = local.getPoin(poin)

    override fun getIngredients(): List<Ingredient> = local.getIngredients()

    override fun getHowTo(): List<HowTo> = local.getHowTo()

    override fun getProvince(): Pair<LiveData<List<Province>>, String?> {
        val client = remote.getProvince()
        val list = MutableLiveData<List<Province>>()
        var error: String? = null
        client.enqueue(object : Callback<ProvinceResponse> {
            override fun onResponse(call: Call<ProvinceResponse>, response: Response<ProvinceResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main?.data!!
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    error = response.message()
                }
            }

            override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                error = t.message
            }
        })
        return Pair(list, error)
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

    override fun getAllCity(): Pair<LiveData<List<City>>, String?> {
        val client = remote.getAllCity()
        val list = MutableLiveData<List<City>>()
        var error: String? = null
        client.enqueue(object : Callback<CitiesResponse> {
            override fun onResponse(call: Call<CitiesResponse>, response: Response<CitiesResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main!!.data
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    error = response.message()
                }
            }

            override fun onFailure(call: Call<CitiesResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                error = t.message
            }
        })
        return Pair(list, error)
    }

    override fun getCost(origin: String, cityId: String, weight: Int, courier: String): Pair<LiveData<List<Costs>>, LiveData<String?>> {
        val client = remote.getCost(origin, cityId, weight, courier)
        val list = MutableLiveData<List<Costs>>()
        val error = MutableLiveData<String?>()
        client.enqueue(object : Callback<CostResponse> {
            override fun onResponse(call: Call<CostResponse>, response: Response<CostResponse>) {
                if (response.isSuccessful) {
                    list.value = response.body()?.main?.list?.get(0)?.costs
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    error.value = response.message()
                }
            }

            override fun onFailure(call: Call<CostResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                error.value = t.message
            }
        })
        return Pair(list, error)
    }
}