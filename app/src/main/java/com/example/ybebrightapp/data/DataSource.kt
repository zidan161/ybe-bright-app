package com.example.ybebrightapp.data

import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.Product

interface DataSource {
    fun getProduct(): List<Product>
    fun getNews()
    fun getAllList(): List<Agent>
    fun getListAgent(poin: Int): List<Agent>
    fun getListReseller(poin: Int): List<Agent>
}