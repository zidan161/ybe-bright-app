package com.example.ybebrightapp.data

import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.Product
import com.example.ybebrightapp.data.local.LocalDataSource

class MainRepository(private val local: LocalDataSource): DataSource {

    companion object {
        @Volatile
        private var INSTANCE: MainRepository? = null

        fun getInstance(local: LocalDataSource): MainRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MainRepository(local).apply { INSTANCE = this }
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
}