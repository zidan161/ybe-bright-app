package com.example.ybebrightapp.product

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.Product
import com.example.ybebrightapp.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(private val context: Context, private val data: List<Product>): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ProductViewHolder(private val binding: ItemProductBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(product: Product) {
            with(binding) {
                tvItemName.text = product.name
                Picasso.get().load(product.image).into(imgItemPhoto)
                this.root.setOnClickListener {
                    val intent = Intent(context, ProductActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}