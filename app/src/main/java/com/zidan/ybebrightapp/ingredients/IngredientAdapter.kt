package com.zidan.ybebrightapp.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidan.ybebrightapp.databinding.ItemIngredientsBinding

class IngredientAdapter(private val data: List<Ingredient>): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val view: ItemIngredientsBinding): RecyclerView.ViewHolder(view.root) {
        fun bindItem(item: Ingredient) {
            with(view) {
                tvName.text = item.name
                tvIngredient.text = item.ingredient
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