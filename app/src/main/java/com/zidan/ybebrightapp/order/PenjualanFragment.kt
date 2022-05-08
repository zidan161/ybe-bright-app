package com.zidan.ybebrightapp.order

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.databinding.FragmentPenjualanBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PenjualanFragment : Fragment() {

    private lateinit var binding: FragmentPenjualanBinding
    private lateinit var db: FirebaseDatabase

    private lateinit var adapter: OrderAdapter
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPenjualanBinding.inflate(layoutInflater, container, false)

        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

        val id = arguments?.getString("id")!!

        val query: Query = db
            .getReference("order")
            .orderByChild("sellerId").equalTo(id)

        val data = FirebaseRecyclerOptions.Builder<Order>()
            .setQuery(query, Order::class.java)
            .build()

        adapter = OrderAdapter(requireContext(), id, data, false) { it, isTrigger ->
            if (it == null && !isTrigger) {
                binding.layoutError.visibility = View.VISIBLE
            } else if (it != null && !isTrigger) {
                binding.layoutError.visibility = View.INVISIBLE
                val intent = Intent(requireContext(), DetailOrderActivity::class.java)
                intent.putExtra("data", it)
                startActivity(intent)
            } else {
                viewModel.triggerLoad()
            }
        }

        if (adapter.snapshots.isEmpty()) {
            binding.layoutError.visibility = View.VISIBLE
        }
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd

        binding.rvOrder.adapter = adapter
        binding.progressBar.visibility = View.INVISIBLE
        binding.rvOrder.layoutManager = manager

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