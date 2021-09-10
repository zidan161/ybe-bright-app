package com.example.ybebrightapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.databinding.CourierLayoutBinding
import com.example.ybebrightapp.model.Costs

class CourierAdapter(private val data: List<Costs>, private val listener: (Costs) -> Unit): RecyclerView.Adapter<CourierAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CourierLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: CourierLayoutBinding, private val listener: (Costs) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Costs) {
            with(binding) {
                tvService.text = item.service
                tvCost.text = item.costs[0].cost.toString()
                root.setOnClickListener { listener(item) }
            }
        }
    }
}