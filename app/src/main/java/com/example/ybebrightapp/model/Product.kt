package com.example.ybebrightapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
        var name: String,
        var image: Int,
        var isPaket: Boolean
): Parcelable
