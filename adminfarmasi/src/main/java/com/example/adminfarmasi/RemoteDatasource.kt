package com.example.adminfarmasi

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RemoteDatasource {

    fun getListChat(db: FirebaseDatabase, ref: String): DatabaseReference = db.getReference(ref)
    fun getChat(db: FirebaseDatabase, ref: String, id: String): DatabaseReference = db.getReference(ref).child(id)
}