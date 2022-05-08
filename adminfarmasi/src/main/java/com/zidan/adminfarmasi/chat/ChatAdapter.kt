package com.zidan.adminfarmasi.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.zidan.adminfarmasi.ChatActivity
import com.zidan.adminfarmasi.R
import com.zidan.adminfarmasi.databinding.ItemChatBinding
import com.zidan.adminfarmasi.model.ListMessages
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ChatAdapter(private val ctx: Context, options: FirebaseRecyclerOptions<ListMessages>,
                  private val listener: (Intent) -> Unit) : FirebaseRecyclerAdapter<ListMessages, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chat, parent, false)
        val binding = ItemChatBinding.bind(view)
        return ChatViewHolder(ctx, binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ListMessages) {
        val id = this.getRef(position).key
        (holder as ChatViewHolder).bind(id, model)
    }

    inner class ChatViewHolder(private val ctx: Context, private val binding: ItemChatBinding,
                               private val listener: (Intent) -> Unit) : ViewHolder(binding.root) {
        fun bind(id: String?, item: ListMessages) {
            binding.tvMsg.text = item.lastChat
            binding.tvName.text = item.name
            binding.root.setOnClickListener {
                val intent = Intent(ctx, ChatActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", item.name)
                listener(intent)
            }
        }
    }
}
