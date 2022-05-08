package com.zidan.ybebrightapp.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.databinding.ItemPriceBinding
import com.zidan.ybebrightapp.model.Price
import com.zidan.ybebrightapp.utils.setDecimal

class PriceAdapter(private val data: List<Price>, private val isPaket: Boolean): RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = ItemPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size

    inner class PriceViewHolder(private val binding: ItemPriceBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(price: Price, pos: Int) {
            with(binding) {
                if (isPaket) {
                    if (pos == data.size - 1) {
                        tvQty.text = "≥ ${price.qty} pcs"
                    } else {
                        tvQty.text = "${price.qty} pcs"
                    }
                } else {
                    tvQty.text = "min. ${price.qty} pcs"
                }
                tvPrice.text = price.harga.setDecimal()
            }
        }
    }
}