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

    override fun getListAgent(poin: Int): List<Agent> = local.getListAgent(poin)

    override fun getListReseller(poin: Int): List<Agent> = local.getListReseller(poin)
}