package com.zidan.ybebrightapp.howto

import com.google.gson.annotations.SerializedName

data class HowTo(
    @SerializedName("NAMA PRODUK")
    var name: String,
    @SerializedName("CARA PENGGUNAAN")
    var howto: String
)
