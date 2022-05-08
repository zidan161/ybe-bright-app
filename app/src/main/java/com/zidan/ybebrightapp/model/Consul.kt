package com.zidan.ybebrightapp.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Consul (
    var text: String,
    var frontPhoto: Uri?,
    var rightPhoto: Uri?,
    var leftPhoto: Uri?
): Parcelable