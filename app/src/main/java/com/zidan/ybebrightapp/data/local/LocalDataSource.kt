package com.zidan.ybebrightapp.data.local

import com.zidan.ybebrightapp.model.Product
import com.zidan.ybebrightapp.data.JsonHelper
import com.zidan.ybebrightapp.howto.HowTo
import com.zidan.ybebrightapp.ingredients.Ingredient
import com.zidan.ybebrightapp.model.Poin

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