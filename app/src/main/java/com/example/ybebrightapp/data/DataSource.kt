package com.example.ybebrightapp.data

import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.Product

interface DataSource {
    fun getProduct(): List<Product>
    fun getNews()
    fun getAllList(): List<Agent>
    fun getListAgentTunggal(nik: String?, poin: Int): Agent?
    fun getListAgent(nik: String?, poin: Int): Agent?
    fun getListReseller(nik: String?, poin: Int): Agent?
}