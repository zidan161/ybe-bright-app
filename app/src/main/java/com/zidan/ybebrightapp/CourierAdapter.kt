package com.zidan.ybebrightapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.databinding.CourierLayoutBinding
import com.zidan.ybebrightapp.model.Costs
import com.zidan.ybebrightapp.utils.setDecimal

class CourierAdapter(private val data: List<Costs>, private val status: String, private val listener: (Costs) -> Unit): RecyclerView.Adapter<CourierAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CourierLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, status, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: CourierLayoutBinding, private val status: String, private val listener: (Costs) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Costs) {
            with(binding) {
                tvService.text = item.service
                tvCost.text = if (status == "Agen Tunggal") {
                    "Free"
                } else {
                    "Rp ${item.costs[0].cost.setDecimal()}"
                }
                root.setOnClickListener { listener(item) }
            }
        }
    }
}