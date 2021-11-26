package com.example.ybebrightapp.agent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.databinding.ItemAgentBinding

class AgentAdapter(private val data: List<Agent>): RecyclerView.Adapter<AgentAdapter.AgentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val binding = ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AgentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class AgentViewHolder(private val binding: ItemAgentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(agent: Agent) {
            with(binding) {
                tvName.text = agent.nama
                tvAddress.text = "${agent.alamat}, ${agent.kota}"
                tvPhone.text = agent.no_hp
            }
        }
    }
}