package com.example.ybebrightapp

import android.content.Context
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

    fun loadProduct() : List<Product> {
        val list = ArrayList<Product>()
        try {
            val responseObject = JSONObject(parsingFileToString("Product.json").toString())
            val listArray = responseObject.getJSONArray("data")
            for (i in 0 until listArray.length()){

                val data = listArray.getJSONObject(i)

                val id = data.getInt("id")

                val product = Product(id)
                list.add(product)
            }
        } catch (e: JSONException){
            e.printStackTrace()
        }

        return list
    }
}