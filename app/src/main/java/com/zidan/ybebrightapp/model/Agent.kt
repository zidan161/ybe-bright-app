package com.zidan.ybebrightapp.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agent(
    var idMember: String = "",
    var idReference: String = "",
    var nama: String = "",
    var point: String = "",
    var nik: String = "",
    var email: String = "",
    var alamat: String = "",
    var kota: String = "",
    var provinsi: String = "",
    var no_hp: String = "",
    var keagenan: String = "",
    var no_rek: String = ""): Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idMember" to idMember,
            "idReference" to idReference,
            "nama" to nama,
            "point" to point,
            "nik" to nik,
            "email" to email,
            "alamat" to alamat,
            "kota" to kota,
            "provinsi" to provinsi,
            "no_hp" to no_hp,
            "keagenan" to keagenan,
            "no_rek" to no_rek
        )
    }
}
