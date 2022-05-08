package com.zidan.ybebrightapp.model

import com.google.gson.annotations.SerializedName

data class CostResponse(@SerializedName("rajaongkir") var main: Results)

data class Results(@SerializedName("results") var list: List<Courier>)

data class Courier(@SerializedName("name") var name: String, @SerializedName("costs") var costs: List<Costs>)

data class Costs (
    @SerializedName("service")
    var service: String,
    @SerializedName("description")
    var des: String,
    @SerializedName("cost")
    var costs: List<Cost>
)

data class Cost(@SerializedName("value") var cost: Int)