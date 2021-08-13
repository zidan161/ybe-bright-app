package com.example.ybebrightapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agent(
    @SerializedName("ID Member")
    var id: String,
    @SerializedName("Nama")
    var name: String,
    @SerializedName("poin")
    var poin: Int,
    @SerializedName("NIK")
    var nik: String,
    @SerializedName("E-mail")
    var email: String = "",
    @SerializedName("Alamat Domisili")
    var address: String = "",
    @SerializedName("Kota")
    var city: String = "",
    @SerializedName("No. Handphone / WhatsApp")
    var phone: String = "",
    var status: String = "Agen"): Parcelable
