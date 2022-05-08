package com.zidan.ybebrightapp.hidok.model

data class FriendlyMessage (
    var text: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var imageUrl: String? = null,
    var isMe: Boolean = true
)
