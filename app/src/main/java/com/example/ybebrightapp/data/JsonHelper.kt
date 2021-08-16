package com.example.ybebrightapp.data

import android.content.Context
import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.Product
import com.example.ybebrightapp.R
import com.google.gson.Gson
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

        val name = ctx.resources.getStringArray(R.array.product_name)
        val img = ctx.resources.obtainTypedArray(R.array.product_image)

        for (i in ctx.resources.getStringArray(R.array.product_name).indices) {
            val product = Product(name[0], img.getResourceId(i, 0))
            data.add(product)
        }
        img.recycle()
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
                    data.put("status", "Agen")
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
}