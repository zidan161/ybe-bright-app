package com.zidan.adminfarmasi.model

class ListMessages{
    var lastChat: String? = null
    var name: String? = null

    constructor()

    constructor(lastChat: String, name: String) {
        this.lastChat = lastChat
        this.name = name
    }
}

class FriendlyMessage {
    var text: String? = null
    var name: String = ""
    var photoUrl: String? = null
    var imageUrl: String? = null
    var isMe: Boolean = false

    // Empty constructor needed for Firestore serialization
    constructor()

    constructor(text: String?, name: String, photoUrl: String?, imageUrl: String?, isMe: Boolean) {
        this.text = text
        this.name = name

        this.photoUrl = photoUrl
        this.imageUrl = imageUrl

        this.isMe = isMe
    }
}
