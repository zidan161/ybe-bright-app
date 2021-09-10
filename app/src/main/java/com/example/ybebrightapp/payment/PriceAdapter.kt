package com.example.ybebrightapp.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.databinding.ItemPriceBinding
import com.example.ybebrightapp.model.Price
import com.example.ybebrightapp.utils.setDecimal
import java.text.DecimalFormat

class PriceAdapter(private val data: List<Price>, private val isJawa: Boolean): RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = ItemPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(data[position], isJawa)
    }

    override fun getItemCount(): Int = data.size

    class PriceViewHolder(private val binding: ItemPriceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(price: Price, isJawa: Boolean) {
            with(binding) {
                tvQty.text = "Qty: ${price.qty}"
                if (isJawa) {
                    tvPrice.text = price.jawaBali.setDecimal()
                } else {
                    tvPrice.text = price.luarJawaBali.setDecimal()
                }
            }
        }
    }
}