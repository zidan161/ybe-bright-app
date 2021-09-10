package com.example.ybebrightapp.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.databinding.ItemAgentBinding
import com.example.ybebrightapp.databinding.ItemCheckoutBinding
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.utils.setDecimal
import com.squareup.picasso.Picasso

class CheckoutAdapter(private val data: ArrayList<Buy>): RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemCheckoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Buy, position: Int) {
            with(binding) {
                tvName.text = item.name
                tvQty.text = "Qty: ${item.count}"
                tvHarga.text = "Harga: ${item.price.setDecimal()}"
                imgDelete.setOnClickListener {
                    data.removeAt(position)
                    notifyItemRemoved(position)
                }
                Picasso.get().load(item.img).into(imgProduct)
            }
        }
    }
}