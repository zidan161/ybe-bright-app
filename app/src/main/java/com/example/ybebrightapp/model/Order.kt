package com.example.ybebrightapp.model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    var name: String? = null,
    var status: String? = null,
    var date: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var buy: List<Buy>? = null,
    var courier: String? = null,
    var cost: Int? = null,
    var isDone: Boolean? = null,
): Parcelable