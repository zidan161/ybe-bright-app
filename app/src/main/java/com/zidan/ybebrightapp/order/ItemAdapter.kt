package com.zidan.ybebrightapp.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.databinding.ItemCheckoutBinding
import com.zidan.ybebrightapp.model.Buy
import com.zidan.ybebrightapp.utils.getImage
import com.zidan.ybebrightapp.utils.setDecimal

class ItemAdapter(private val data: List<Buy>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: ItemCheckoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Buy) {
            with(binding) {
                btnRemove.visibility = View.GONE
                tvName.text = item.name
                tvHarga.text = item.price.setDecimal()
                tvQty.text = "Qty: ${item.count}"

                val image = getImage(item.name)

                Glide.with(root).load(image).into(imgProduct)
            }
        }
    }
}