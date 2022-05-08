package com.zidan.adminfarmasi.chat

class Recipe {
    var id: String? = null
    var name: String? = null
    var des: String? = null

    constructor()

    constructor(id: String, name: String, des: String) {
        this.id = id
        this.name = name
        this.des = des
    }
}
