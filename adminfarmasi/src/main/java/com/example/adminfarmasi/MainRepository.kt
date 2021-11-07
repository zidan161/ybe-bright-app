package com.example.adminfarmasi

import com.example.adminfarmasi.model.FriendlyMessage
import com.example.adminfarmasi.model.ListMessages
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainRepository(private val remote: RemoteDatasource): DataSource {

    companion object {
        @Volatile
        private var INSTANCE: MainRepository? = null

        fun getInstance(remote: RemoteDatasource): MainRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MainRepository(remote).apply { INSTANCE = this }
        }
    }

    override fun getListCustChat(db: FirebaseDatabase): FirebaseRecyclerOptions<ListMessages> {
        val ref = remote.getListChat(db, MainActivity.LAST_CUST)
        return FirebaseRecyclerOptions.Builder<ListMessages>()
            .setQuery(ref, ListMessages::class.java)
            .build()
    }

    override fun getListAdminChat(db: FirebaseDatabase): FirebaseRecyclerOptions<ListMessages> {
        val ref = remote.getListChat(db, MainActivity.LAST_ADMIN)
        return FirebaseRecyclerOptions.Builder<ListMessages>()
            .setQuery(ref, ListMessages::class.java)
            .build()
    }

    override fun getCustChat(db: FirebaseDatabase, id: String): FirebaseRecyclerOptions<FriendlyMessage> {
        val ref = remote.getChat(db, ChatActivity.CUST, id)
        return FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(ref, FriendlyMessage::class.java)
            .build()
    }

    override fun getAdminChat(db: FirebaseDatabase, id: String): FirebaseRecyclerOptions<FriendlyMessage> {
        val ref = remote.getChat(db, ChatActivity.ADMIN, id)
        return FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(ref, FriendlyMessage::class.java)
            .build()
    }
}