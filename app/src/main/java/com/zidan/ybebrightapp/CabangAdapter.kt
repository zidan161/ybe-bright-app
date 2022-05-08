package com.zidan.ybebrightapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.databinding.CabangItemBinding
import com.zidan.ybebrightapp.model.Agent

class CabangAdapter(private val data: List<Agent>, private val listener: (Agent) -> Unit): RecyclerView.Adapter<CabangAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CabangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: CabangItemBinding, private val listener: (Agent) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(agent: Agent) {
            binding.tvKota.text = agent.kota
            binding.tvData.text = "- ${agent.nama}"
            binding.root.setOnClickListener { listener(agent) }
        }
    }
}