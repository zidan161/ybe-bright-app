package com.zidan.ybebrightapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.databinding.CabangKotaItemBinding
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.model.Cabang

class KotaCabangAdapter(
    private val ctx: Context,
    private val data: List<Cabang>,
    private val listener: (Agent) -> Unit): RecyclerView.Adapter<KotaCabangAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CabangKotaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(ctx, binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(
        private val ctx: Context,
        private val binding: CabangKotaItemBinding,
        private val listener: (Agent) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(cabang: Cabang) {
            binding.tvKota.text = cabang.name
            binding.tvKota.setOnClickListener {
                if (!binding.expandCabang.isExpanded) {
                    binding.expandCabang.expand()
                } else {
                    binding.expandCabang.collapse()
                }
            }
            val adapter = CabangAdapter(cabang.data) { listener(it) }
            binding.rvKota.adapter = adapter
            binding.rvKota.layoutManager = LinearLayoutManager(ctx)
        }
    }
}