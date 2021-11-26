package com.example.ybebrightapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Member

@Parcelize
data class Agent(
    var idMember: String = "",
    var nama: String = "",
    var point: String = "",
    var nik: String = "",
    var email: String = "",
    var alamat: String = "",
    var kota: String = "",
    var no_hp: String = "",
    var keagenan: String = "",
    var no_rek: String = ""): Parcelable
