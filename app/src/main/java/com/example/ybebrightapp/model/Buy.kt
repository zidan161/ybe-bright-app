package com.example.ybebrightapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Buy (
    var name: String,
    var img: Int,
    var count: Int,
    var price: Int,
    var total: Int
): Parcelable