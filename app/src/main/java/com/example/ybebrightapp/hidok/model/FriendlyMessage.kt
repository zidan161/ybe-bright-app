package com.example.ybebrightapp.hidok.model

class FriendlyMessage {
    var text: String? = null
    var name: String? = null
    var photoUrl: String? = null
    var imageUrl: String? = null
    var isMe: Boolean = true

    // Empty constructor needed for Firestore serialization
    constructor()

    constructor(text: String?, name: String?, photoUrl: String?, imageUrl: String?, isMe: Boolean) {
        this.text = text
        this.name = name

        this.photoUrl = photoUrl
        this.imageUrl = imageUrl

        this.isMe = isMe
    }
}
