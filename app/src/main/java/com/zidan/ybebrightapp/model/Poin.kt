package com.zidan.ybebrightapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Poin (
    @SerializedName("poin")
    var poin: Int,
    @SerializedName("rupiah")
    var rupiah: Int,
    @SerializedName("tukar")
    var tukar: String
): Parcelable