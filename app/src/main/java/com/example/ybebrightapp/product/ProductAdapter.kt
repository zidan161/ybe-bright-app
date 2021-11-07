package com.example.ybebrightapp.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.databinding.ItemProductBinding

class ProductAdapter(private val data: List<Product>,
                     private var status: String?,
                     private val listener: (Product, String?) -> Unit): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItem(data[position], status)
    }

    override fun getItemCount(): Int = data.size

    class ProductViewHolder(private val binding: ItemProductBinding, private val listener: (Product, String?) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(product: Product, status: String?) {
            with(binding) {
                tvItemName.text = product.name
                Glide.with(root).load(product.image).into(imgItemPhoto)
                if (product.isPaket) {
                    binding.tvPaket.visibility = View.VISIBLE
                }
                btnHarga.setOnClickListener {
                    listener(product, status)
                }
            }
        }
    }
}