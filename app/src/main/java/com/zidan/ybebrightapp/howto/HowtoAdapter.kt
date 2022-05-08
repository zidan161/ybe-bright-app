package com.zidan.ybebrightapp.howto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.ItemHowtoBinding

class HowtoAdapter(private val data: List<HowTo>): RecyclerView.Adapter<HowtoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHowtoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val view: ItemHowtoBinding): RecyclerView.ViewHolder(view.root) {
        fun bindItem(item: HowTo) {
            with(view) {
                when (item.name) {
                    "Day Cream" -> {
                        Glide.with(root).load(R.drawable.daycream_howto).into(imgItem)
                    } "Night Cream" -> {
                        Glide.with(root).load(R.drawable.nightcream_howto).into(imgItem)
                    } "Facial Wash" -> {
                        Glide.with(root).load(R.drawable.facialwash_howto).into(imgItem)
                    } "Facial Wash Acne" -> {
                        Glide.with(root).load(R.drawable.facialacne_howto).into(imgItem)
                    } "Serum Brightening" -> {
                        Glide.with(root).load(R.drawable.serumbrightening_howto).into(imgItem)
                    } "Serum Acne" -> {
                        Glide.with(root).load(R.drawable.serumacne_howto).into(imgItem)
                    } "Toner" -> {
                        Glide.with(root).load(R.drawable.toner_howto).into(imgItem)
                    } else -> {
                        Glide.with(root).load(R.drawable.refreshfacecleansing_howto).into(imgItem)
                    }
                }
                tvName.text = item.name
                tvHowto.text = item.howto
                tvName.setOnClickListener {
                    if (!expandable.isExpanded) {
                        expandable.expand()
                    } else {
                        expandable.collapse()
                    }
                }
            }
        }
    }
}