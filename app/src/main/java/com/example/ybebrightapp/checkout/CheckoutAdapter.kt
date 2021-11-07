package com.example.ybebrightapp.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ybebrightapp.databinding.ItemCheckoutBinding
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.utils.setDecimal

class CheckoutAdapter(private val listener: (Int) -> Unit): RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    var itemList = mutableListOf<Buy>()

    fun setData(item: MutableList<Buy>) {
        itemList = item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ViewHolder(private val binding: ItemCheckoutBinding, private val listener:
        (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Buy, position: Int) {
            with(binding) {
                tvName.text = item.name
                tvQty.text = "Qty: ${item.count}"
                tvHarga.text = item.price.setDecimal()
                //tunggu disuruh
//                imgDelete.setOnClickListener {
//                    itemList.removeAt(position)
//                    notifyItemRemoved(position)
//                    listener(position)
//                }
                Glide.with(binding.root).load(item.img).into(imgProduct)
            }
        }
    }
}