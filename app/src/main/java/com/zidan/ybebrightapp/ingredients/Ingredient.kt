package com.zidan.ybebrightapp.ingredients

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("name")
    var name: String,
    @SerializedName("ingredients")
    var ingredient: String)
