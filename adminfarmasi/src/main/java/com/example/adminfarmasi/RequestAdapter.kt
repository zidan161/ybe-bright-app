package com.example.adminfarmasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.adminfarmasi.chat.Recipe
import com.example.adminfarmasi.databinding.CardRequestBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class RequestAdapter(options: FirebaseRecyclerOptions<Recipe>, private val listener: (Recipe) -> Unit) : FirebaseRecyclerAdapter<Recipe, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_request, parent, false)
        val binding = CardRequestBinding.bind(view)
        return ChatViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Recipe) {
        (holder as ChatViewHolder).bind(model)
    }

    inner class ChatViewHolder(private val binding: CardRequestBinding, private val listener: (Recipe) -> Unit) : ViewHolder(binding.root) {
        fun bind(item: Recipe) {
            binding.tvId.text = item.id
            binding.tvDes.text = item.des
            binding.tvName.text = item.name
            binding.root.setOnClickListener { listener(item) }
        }
    }
}