package com.example.ybebrightapp.data

import androidx.lifecycle.LiveData
import com.example.ybebrightapp.model.*
import okhttp3.RequestBody

interface DataSource {
    fun getProduct(): List<Product>
    fun getNews()
    fun getAllList(): List<Agent>
    fun getListAgentTunggal(nik: String?, poin: Int): Agent?
    fun getListAgent(nik: String?, poin: Int): Agent?
    fun getListReseller(nik: String?, poin: Int): Agent?
    fun getProvince(): LiveData<List<Province>>
    fun getCity(id: String): LiveData<List<City>>
    fun getCost(request: RequestBody): LiveData<List<Costs>>
    fun getPoin(poin: Int): List<Poin>
}