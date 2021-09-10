package com.example.ybebrightapp.data.local

import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.data.JsonHelper
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

    fun getListAuto(): List<Agent> = helper.loadAll()

    fun getListAgentTunggal(nik: String?, poin: Int): Agent? = helper.loadAgentTunggal(nik, poin)

    fun getListAgent(nik: String?, poin: Int): Agent? = helper.loadAgent(nik, poin)

    fun getListReseller(nik: String?, poin: Int): Agent? = helper.loadReseller(nik, poin)

    fun getPoin(poin: Int): List<Poin> = helper.loadPoin(poin)
}