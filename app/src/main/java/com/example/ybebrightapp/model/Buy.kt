package com.example.ybebrightapp.model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Buy (
    @PropertyName("nama_barang")
    var name: String?,
    var img: Int?,
    @PropertyName("qty")
    var count: Int,
    @PropertyName("harga")
    var price: Int,
    @PropertyName("total")
    var total: Int): Parcelable