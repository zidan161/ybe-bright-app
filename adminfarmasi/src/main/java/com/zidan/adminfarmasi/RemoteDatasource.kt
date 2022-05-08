package com.zidan.adminfarmasi

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RemoteDatasource {

    fun getListChat(db: FirebaseDatabase, ref: String): DatabaseReference = db.getReference(ref)
    fun getChat(db: FirebaseDatabase, ref: String, id: String): DatabaseReference = db.getReference(ref).child(id)
}