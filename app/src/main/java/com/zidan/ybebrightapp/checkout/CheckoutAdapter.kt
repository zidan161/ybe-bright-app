package com.zidan.ybebrightapp.checkout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.databinding.ItemCheckoutBinding
import com.zidan.ybebrightapp.model.Buy
import com.zidan.ybebrightapp.utils.setDecimal

class CheckoutAdapter(private val listener: (List<Buy>, Buy) -> Unit): RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    var itemList = mutableListOf<Buy>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: MutableList<Buy>) {
        itemList = item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ViewHolder(private val binding: ItemCheckoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Buy, position: Int) {
            with(binding) {
                tvName.text = item.name
                tvQty.text = "Qty: ${item.count}"
                tvHarga.text = "Rp. ${item.price.setDecimal()}"

                //tunggu disuruh
                btnRemove.setOnClickListener {
                    itemList.remove(item)
                    notifyItemRemoved(position)
                    listener(itemList, item)
                }
                Glide.with(binding.root).load(item.img).into(imgProduct)
            }
        }
    }
}