package com.zidan.ybebrightapp.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.CardOrderBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.zidan.ybebrightapp.utils.getImage

class OrderAdapter(private val ctx: Context,
                   private val id: String,
                   options: FirebaseRecyclerOptions<Order>,
                   private val isPembelian: Boolean,
                   private val listener: (Order?, Boolean) -> Unit) : FirebaseRecyclerAdapter<Order, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_order, parent, false)
        val binding = CardOrderBinding.bind(view)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Order) {
        (holder as ChatViewHolder).bind(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        if(snapshots.isEmpty()) {
            listener(null, false)
        }
    }

    inner class ChatViewHolder(private val binding: CardOrderBinding) : ViewHolder(binding.root) {
        fun bind(item: Order) {
            val preferences = ctx.getSharedPreferences("myShared", AppCompatActivity.MODE_PRIVATE)
            with(binding) {
                val item1 = item.buy!![0]
                val image = getImage(item1.name)
                Glide.with(root).load(image).into(imgProduct)
                tvProdukName.text = item1.name
                tvQty.text = "Jumlah Order: ${item.buy?.size}"
                tvQty1.text = "Qty: ${item1.count}"
                tvDate.text = item.date
                if (item.buy!!.size > 1) {
                    tvSelengkapnya.text = "dan ${item.buy!!.size - 1} lainnya"
                }
                if (isPembelian) {
                    btnDone.text = "Terima"
                } else {
                    tvPenjualan.visibility = View.VISIBLE
                    tvPenjualan.text = item.name
                }
                btnDone.setOnClickListener {
                    for (i in item.buy!!) {
                        val exist = preferences.getInt("$id${i.name}", 0)
                        if (isPembelian) {
                            preferences.edit().putInt("$id${i.name}", exist + i.count).apply()
                        } else {
                            preferences.edit().putInt("$id${i.name}", exist - i.count).apply()
                        }
                    }
                    listener(null, true)
                }
                btnDetail.setOnClickListener { listener(item, false) }
            }
        }
    }
}