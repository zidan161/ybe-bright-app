package com.zidan.ybebrightapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.ybebrightapp.howto.HowTo
import com.zidan.ybebrightapp.ingredients.Ingredient
import com.zidan.ybebrightapp.model.*
import com.zidan.ybebrightapp.model.Agent

interface DataSource {
    fun getProduct(): List<Product>
    fun getAllMember(): MutableLiveData<MutableList<Agent>?>
    fun getMemberByValue(id: String?): MutableLiveData<MutableList<Cabang>?>
    fun getMember(id: String): MutableLiveData<Agent?>
    fun getProvince(): Pair<LiveData<List<Province>>, String?>
    fun getCity(id: String): LiveData<List<City>>
    fun getAllCity(): Pair<LiveData<List<City>>, String?>
    fun getCost(origin: String, cityId: String, weight: Int, courier: String): Pair<LiveData<List<Costs>>, LiveData<String?>>
    fun getPoin(poin: Int): List<Poin>
    fun getIngredients(): List<Ingredient>
    fun getHowTo(): List<HowTo>
}