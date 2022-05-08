package com.zidan.ybebrightapp.order

import android.os.Parcelable
import com.zidan.ybebrightapp.model.Buy
import kotlinx.parcelize.Parcelize

@Parcelize
class Order (
    var name: String? = null,
    var status: String? = null,
    var date: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var buy: List<Buy>? = null,
    var courier: String? = null,
    var cost: Int? = null,
    var done: Boolean? = null
): Parcelable
