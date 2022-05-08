package com.zidan.ybebrightapp.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.databinding.ItemStockBinding
import com.zidan.ybebrightapp.model.Stock

class StockAdapter(private val list: MutableList<Stock>): RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateData(name: String, count: Int) {
        list.find { it.name == name }?.stock = count
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemStockBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Stock) {
            Glide.with(binding.root).load(item.image).into(binding.imgProduct)
            binding.tvName.text = item.name
            if (item.stock == 0) {
                binding.tvQty.text = "Habis"
            } else {
                binding.tvQty.text = "${item.stock} .pcs"
            }
        }
    }
}