package com.example.adminfarmasi

import androidx.lifecycle.ViewModel
import com.example.adminfarmasi.model.ListMessages
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainViewModel(private val repository: MainRepository): ViewModel() {

    fun getListChatCust(db: FirebaseDatabase) = repository.getListCustChat(db)
    fun getListChatAdmin(db: FirebaseDatabase) = repository.getListAdminChat(db)
}