package com.zidan.adminfarmasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.adminfarmasi.chat.ChatAdapter
import com.zidan.adminfarmasi.databinding.FragmentChatCustomerBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatCustomerFragment : Fragment() {

    private lateinit var binding: FragmentChatCustomerBinding
    private lateinit var db: FirebaseDatabase

    private lateinit var adapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentChatCustomerBinding.inflate(layoutInflater, container, false)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]

        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val data = viewModel.getListChatCust(db)

        adapter = ChatAdapter(requireContext(), data) {
            it.putExtra("ref", ChatActivity.CUST)
            startActivity(it)
        }
        val manager = LinearLayoutManager(requireContext())

        binding.rvCustomer.adapter = adapter
        binding.rvCustomer.layoutManager = manager

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