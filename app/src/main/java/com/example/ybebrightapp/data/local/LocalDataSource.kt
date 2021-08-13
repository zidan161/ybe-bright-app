package com.example.ybebrightapp.data.local

import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.Product
import com.example.ybebrightapp.R
import com.example.ybebrightapp.data.JsonHelper

class LocalDataSource (private val helper: JsonHelper) {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(helper: JsonHelper): LocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LocalDataSource(helper).apply { INSTANCE = this }
        }
    }

    fun getProducts(): List<Product> = helper.loadProduct()

    fun getListAuto(): List<Agent> = helper.loadAll()

    fun getListAgent(poin: Int): List<Agent> = helper.loadAgent(poin)

    fun getListReseller(poin: Int): List<Agent> = helper.loadReseller(poin)
}