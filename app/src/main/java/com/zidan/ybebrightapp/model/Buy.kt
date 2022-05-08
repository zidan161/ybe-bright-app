package com.zidan.ybebrightapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Buy (
    var name: String = "",
    var img: Int? = null,
    var count: Int = 0,
    var price: Int = 0,
    var total: Int = 0,
    var weight: Int = 0,
    var isPaket: Boolean = false): Parcelable