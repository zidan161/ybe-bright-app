package com.zidan.adminfarmasi

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.adminfarmasi.chat.FriendlyMessageAdapter
import com.zidan.adminfarmasi.databinding.ChatActivityBinding
import com.zidan.adminfarmasi.model.FriendlyMessage
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    companion object {
        const val CUST = "chat"
        const val ADMIN = "admin_chat"
    }

    private lateinit var binding: ChatActivityBinding
    private lateinit var manager: LinearLayoutManager

    // Firebase instance variables
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FriendlyMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")!!
        val name = intent.getStringExtra("name")!!
        val reference = intent.getStringExtra("ref")!!

        // Initialize Realtime Database
        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val messagesRef = db.getReference(reference).child(id)

        supportActionBar?.title = name

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(messagesRef, FriendlyMessage::class.java)
            .build()
        adapter = FriendlyMessageAdapter(this, options)
        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        )

        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))

        binding.sendButton.setOnClickListener {
            val lastChat = binding.messageEditText.text.toString()
            val friendlyMessage = FriendlyMessage(
                lastChat,
                name,
                null,
                null,
                false
            )
            db.getReference(reference).child(id).push().setValue(friendlyMessage)
            binding.messageEditText.setText("")
        }
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
}