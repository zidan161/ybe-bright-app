package com.zidan.ybebrightapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
        var name: String,
        var image: Int,
        var weight: Int,
        var isPaket: Boolean
): Parcelable
