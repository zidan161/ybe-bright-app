package com.zidan.ybebrightapp.agent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.databinding.ItemAgentBinding

class AgentAdapter(private val data: List<Agent>): RecyclerView.Adapter<AgentAdapter.AgentViewHolder>(), Filterable {

    var dataFix = listOf<Agent>()

    fun setData(item: List<Agent>) {
        dataFix = item
        notifyDataSetChanged()
    }

    init {
        setData(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val binding = ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AgentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        if (dataFix.isNotEmpty()) {
            holder.bind(dataFix[position])
        }
    }

    override fun getItemCount(): Int = dataFix.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val countryFilterList: List<Agent>
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()) {
                    data
                } else {
                    val resultList = mutableListOf<Agent>()
                    for (member in dataFix) {
                        if (member.kota.lowercase().contains(charSearch.lowercase())) {
                            resultList.add(member)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    setData(results.values as MutableList<Agent>)
                }
            }
        }
    }

    class AgentViewHolder(private val binding: ItemAgentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(agent: Agent) {
            with(binding) {
                tvName.text = agent.nama
                tvAddress.text = agent.alamat
                tvPhone.text = agent.no_hp
            }
        }
    }
}