package com.example.ybebrightapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

data class ProvinceResponse(@SerializedName("rajaongkir") var main: Provinces)

data class Provinces(@SerializedName("results") var data: List<Province>)

@Parcelize
data class Province(
    @SerializedName("province_id")
    var id: String,
    @SerializedName("province")
    var name: String
): Parcelable
