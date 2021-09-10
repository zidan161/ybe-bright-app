package com.example.ybebrightapp.model

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
    var nik: String?,
    @SerializedName("E-mail")
    var email: String = "",
    @SerializedName("Alamat Domisili")
    var address: String = "",
    @SerializedName("Kota")
    var city: String = "",
    @SerializedName("No. Handphone / WhatsApp")
    var phone: String = "",
    @SerializedName("status")
    var status: String,
    @SerializedName("No. Rekening")
    var rek: String?): Parcelable
