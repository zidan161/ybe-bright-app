package com.zidan.ybebrightapp.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.ItemHargaBinding
import com.zidan.ybebrightapp.utils.setDecimal

class HargaProdukAdapter(private val data: List<Map<String, Int>>): RecyclerView.Adapter<HargaProdukAdapter.ViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = ItemHargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val statuses = listOf("Agen Tunggal", "Agen", "Reseller", "customer")
        holder.bindItem(data[position], statuses[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewModel(private val binding: ItemHargaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(harga: Map<String, Int>, status: String) {
            with(binding) {
                val minimal: String =
                    if (status == "Agen Tunggal" || status == "Agen") { "10" }
                    else if (status == "Reseller") { "3" }
                    else { "1" }

                when (status) {
                    "Agen" -> {
                        tvStatus.setBackgroundResource(R.color.tosca)
                    }
                    "Reseller" -> {
                        tvStatus.setBackgroundResource(R.color.green)
                    }
                    "customer" -> {
                        tvStatus.setBackgroundResource(R.color.purple_200)
                    }
                }
                tvStatus.text = status
                tvHargaDayCream.text = harga["Day Cream"]?.setDecimal()
                tvHargaNightCream.text = harga["Night Cream"]?.setDecimal()
                tvHargaFacial.text = harga["Facial Wash"]?.setDecimal()
                tvHargaFacialAcne.text = harga["Facial Wash Acne"]?.setDecimal()
                tvHargaSerumB.text = harga["Serum Brightening"]?.setDecimal()
                tvHargaSerumA.text = harga["Serum Acne"]?.setDecimal()
                tvHargaToner.text = harga["Toner"]?.setDecimal()
                tvHargaRefresh.text = harga["Refresh Face Cleansing"]?.setDecimal()
                tvMinOrder.text = "$minimal pcs"
            }
        }
    }
}