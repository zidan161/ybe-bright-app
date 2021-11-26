package com.example.ybebrightapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    @SerializedName("Qty")
    var qty: String,
    @SerializedName("Jawa Bali")
    var harga: Int,
    @SerializedName("Luar Jawa Bali")
    var luarJawaBali: Int
): Parcelable
