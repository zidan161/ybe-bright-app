package com.example.ybebrightapp.model

import com.google.gson.annotations.SerializedName

data class CitiesResponse(@SerializedName("rajaongkir") var main: Cities)

data class Cities(@SerializedName("results") var data: List<City>)

data class City(
    @SerializedName("city_id")
    var id: String,
    @SerializedName("province_id")
    var provinceId: String,
    @SerializedName("province")
    var provinceName: String,
    @SerializedName("city_name")
    var name: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("postal_code")
    var postalCode: String
)
