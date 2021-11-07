package com.example.adminfarmasi

import com.example.adminfarmasi.model.FriendlyMessage
import com.example.adminfarmasi.model.ListMessages
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

interface DataSource {

    fun getListCustChat(db: FirebaseDatabase): FirebaseRecyclerOptions<ListMessages>
    fun getListAdminChat(db: FirebaseDatabase): FirebaseRecyclerOptions<ListMessages>
    fun getCustChat(db: FirebaseDatabase, id: String): FirebaseRecyclerOptions<FriendlyMessage>
    fun getAdminChat(db: FirebaseDatabase, id: String): FirebaseRecyclerOptions<FriendlyMessage>
}