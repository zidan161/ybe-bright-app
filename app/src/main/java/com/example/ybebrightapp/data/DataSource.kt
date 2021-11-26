package com.example.ybebrightapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ybebrightapp.howto.HowTo
import com.example.ybebrightapp.ingredients.Ingredient
import com.example.ybebrightapp.model.*
import okhttp3.RequestBody

interface DataSource {
    fun getProduct(): List<Product>
    fun getAllMember(): MutableLiveData<MutableList<Agent>?>
    fun getMember(id: String): MutableLiveData<Agent?>
    fun getProvince(): LiveData<List<Province>>
    fun getCity(id: String): LiveData<List<City>>
    fun getCost(request: RequestBody): LiveData<List<Costs>>
    fun getPoin(poin: Int): List<Poin>
    fun getIngredients(): List<Ingredient>
    fun getHowTo(): List<HowTo>
}