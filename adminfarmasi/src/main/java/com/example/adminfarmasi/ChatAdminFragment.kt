package com.example.adminfarmasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfarmasi.chat.ChatAdapter
import com.example.adminfarmasi.chat.Recipe
import com.example.adminfarmasi.databinding.FragmentChatAdminBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatAdminFragment : Fragment() {

    private lateinit var binding: FragmentChatAdminBinding
    private lateinit var db: FirebaseDatabase

    private lateinit var adapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentChatAdminBinding.inflate(layoutInflater, container, false)

        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val messagesRef = db.getReference("recipe")

        val data = FirebaseRecyclerOptions.Builder<Recipe>()
            .setQuery(messagesRef, Recipe::class.java)
            .build()

        adapter = RequestAdapter(data) {
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            intent.putExtra("name", it.name)
            intent.putExtra("id", it.id)
            intent.putExtra("des", it.des)
            startActivity(intent)
        }
        binding.rvAdmin.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        binding.rvAdmin.layoutManager = manager

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}