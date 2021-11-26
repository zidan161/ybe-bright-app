package com.example.ybebrightapp.data.local

import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.howto.HowTo
import com.example.ybebrightapp.ingredients.Ingredient
import com.example.ybebrightapp.model.Poin

class LocalDataSource (private val helper: JsonHelper) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(helper: JsonHelper): LocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LocalDataSource(helper).apply { INSTANCE = this }
        }
    }

    fun getProducts(): List<Product> = helper.loadProduct()

    fun getPoin(poin: Int): List<Poin> = helper.loadPoin(poin)

    fun getIngredients(): List<Ingredient> = helper.loadIngredient()

    fun getHowTo(): List<HowTo> = helper.loadHowTo()
}