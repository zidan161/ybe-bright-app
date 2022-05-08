package com.zidan.ybebrightapp.data

import android.content.Context
import com.zidan.ybebrightapp.model.Product
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.howto.HowTo
import com.zidan.ybebrightapp.ingredients.Ingredient
import com.zidan.ybebrightapp.model.Poin
import com.zidan.ybebrightapp.model.Price
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
            val weight = ctx.resources.getIntArray(R.array.product_weight)

            for (i in name.indices) {
                val product = if (i.equals(0..1)) {
                    Product(name[0], img.getResourceId(i, 0), weight[i], true)
                } else {
                    Product(name[0], img.getResourceId(i, 0), weight[i],false)
                }
                data.add(product)
            }
            img.recycle()
        }
        return data
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

    fun loadPriceByStatus(status: String): Map<String, Int> {
        val finalPrice = mutableMapOf<String, Int>()
        val responseObject = JSONObject(parsingFileToString("harga_satuan.json").toString())
        try {
            val allProduk = listOf("Facial Wash", "Facial Wash Acne", "Toner", "Serum Brightening", "Serum Acne", "Day Cream", "Night Cream", "Refresh Face Cleansing")
            for (a in 0 until responseObject.length()) {
                val listArray = responseObject.getJSONArray(allProduk[a])
                for (i in 0 until listArray.length()) {

                    val data = listArray.getJSONObject(i)
                    if (data.getString("status") == status) {
                        finalPrice[allProduk[a]] = data.getInt("Jawa Bali")
                    }
                }
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

    fun loadHowTo(): ArrayList<HowTo> {
        val list = ArrayList<HowTo>()
        try {
            val responseObject = JSONObject(parsingFileToString("howto.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)
                val gson = Gson()

                val how = gson.fromJson(data.toString(), HowTo::class.java)
                list.add(how)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }
}