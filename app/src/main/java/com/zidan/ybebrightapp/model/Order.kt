package com.zidan.ybebrightapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    var buyerId: String? = null,
    var sellerId: String? = null,
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