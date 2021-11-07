package com.example.ybebrightapp.data

import android.content.Context
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.R
import com.example.ybebrightapp.ingredients.Ingredient
import com.example.ybebrightapp.model.Poin
import com.example.ybebrightapp.model.Price
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class JsonHelper(private val ctx: Context) {

    private fun parsingFileToString(fileName: String): String? {
        return try {
            val `is` = ctx.assets.open(fileName)
            val buffer = ByteArray(`is`.available())
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException){
            ex.printStackTrace()
            null
        }
    }

    fun loadProduct(): List<Product> {
        val data = mutableListOf<Product>()

        CoroutineScope(Dispatchers.Default).launch {
            val name = ctx.resources.getStringArray(R.array.product_name)
            val img = ctx.resources.obtainTypedArray(R.array.product_image)

            for (i in name.indices) {
                val product = if (i.equals(0..1)) {
                    Product(name[0], img.getResourceId(i, 0), true)
                } else {
                    Product(name[0], img.getResourceId(i, 0), false)
                }
                data.add(product)
            }
            img.recycle()
        }
        return data
    }

    fun loadAll() : List<Agent> {
        val list = ArrayList<Agent>()
        try {
            val responseObject = JSONObject(parsingFileToString("auto.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                val gson = Gson()

                val agent = gson.fromJson(data.toString(), Agent::class.java)
                list.add(agent)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }

    fun loadAgentTunggal(nik: String?, poin: Int): Agent? {
        var trueAgent: Agent? = null
        try {
            val responseObject = JSONObject(parsingFileToString("agen_tunggal.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                    if (data.getString("NIK").equals(nik)) {
                        data.put("status", "agen tunggal")
                        data.put("poin", poin)
                        val gson = Gson()

                    trueAgent = gson.fromJson(data.toString(), Agent::class.java)
                }
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }
        return trueAgent
    }

    fun loadAgent(nik: String?, poin: Int): Agent? {
        var trueAgent: Agent? = null
        try {
            val responseObject = JSONObject(parsingFileToString("agen.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                if (data.getString("NIK").equals(nik)) {
                    data.put("status", "Reseller")
                    data.put("poin", poin)
                    val gson = Gson()

                    trueAgent = gson.fromJson(data.toString(), Agent::class.java)
                }
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }
        return trueAgent
    }

    fun loadReseller(nik: String?, poin: Int): Agent? {
        var trueAgent: Agent? = null
        try {
            val responseObject = JSONObject(parsingFileToString("reseller.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                if (data.getString("NIK").equals(nik)) {
                    data.put("status", "Reseller")
                    data.put("poin", poin)
                    val gson = Gson()

                    trueAgent = gson.fromJson(data.toString(), Agent::class.java)
                }
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }
        return trueAgent
    }

    fun loadPrice(fileName: String, arrayName: String): ArrayList<Price> {
        val finalPrice = ArrayList<Price>()
        val responseObject = JSONObject(parsingFileToString(fileName).toString())
            try {
                val listArray = responseObject.getJSONArray(arrayName)
                for (i in 0 until listArray.length()) {

                    val data = listArray.getJSONObject(i)

                    val gson = Gson()

                    finalPrice.add(gson.fromJson(data.toString(), Price::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        return finalPrice
    }

    fun loadPoin(poin: Int): ArrayList<Poin> {
        val finalPrice = ArrayList<Poin>()
        try {
            val responseObject = JSONObject(parsingFileToString("poin.json").toString())
            val listArray = responseObject.getJSONArray("data")

            val size: Int = when (poin) {
                in 50..99 -> { 1 }
                in 100..149 -> { 2 }
                in 150..199 -> { 3 }
                in 200..299 -> { 4 }
                in 300..449 -> { 5 }
                in 450..499 -> { 6 }
                in 500..749 -> { 7 }
                in 750..999 -> { 8 }
                in 1000..1499 -> { 9 }
                in 1500..1999 -> { 10 }
                in 2000..2999 -> { 11 }
                in 3000..3499 -> { 12 }
                in 3500..17999 -> { 13 }
                in 18000..29999 -> { 14 }
                in 30000..69999 -> { 15 }
                in 70000..999999 -> { 16 }
                else -> { listArray.length() }
            }

            for (i in 0 until size){

                val data = listArray.getJSONObject(i)
                val gson = Gson()

                finalPrice.add(gson.fromJson(data.toString(), Poin::class.java))
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }
        return finalPrice
    }

    fun loadIngredient(): ArrayList<Ingredient> {
        val list = ArrayList<Ingredient>()
        try {
            val responseObject = JSONObject(parsingFileToString("ingredients.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                val gson = Gson()

                val ing = gson.fromJson(data.toString(), Ingredient::class.java)
                list.add(ing)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }
}