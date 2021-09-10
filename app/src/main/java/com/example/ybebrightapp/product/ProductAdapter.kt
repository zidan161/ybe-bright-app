package com.example.ybebrightapp.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(private val ctx: Context,
                     private val data: List<Product>,
                     private var status: String?,
                     private val listener: (Bundle) -> Unit): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItem(ctx, data[position], status)
    }

    override fun getItemCount(): Int = data.size

    class ProductViewHolder(private val binding: ItemProductBinding, private val listener: (Bundle) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(ctx: Context, product: Product, status: String?) {
            with(binding) {
                tvItemName.text = product.name
                Picasso.get().load(product.image).into(imgItemPhoto)
                if (product.isPaket) {
                    binding.tvPaket.visibility = View.VISIBLE
                }
                btnHarga.setOnClickListener {

                    val helper = JsonHelper(ctx)
                    val bundle = Bundle()

                    val data = if (product.isPaket) {
                        helper.loadPrice("harga_paket.json", status!!)
                    } else {
                        helper.loadPrice("harga_satuan.json", product.name)
                    }

                    if (product.isPaket) {
                        bundle.putParcelableArrayList("price", data)
                    } else {
                        when (status) {
                            "agen tunggal" -> {
                                bundle.putParcelableArrayList("price", arrayListOf(data[3]))
                            }
                            "agen" -> {
                                bundle.putParcelableArrayList("price", arrayListOf(data[2]))
                            }
                            "reseller" -> {
                                bundle.putParcelableArrayList("price", arrayListOf(data[1]))
                            }
                            else -> {
                                bundle.putParcelableArrayList("price", arrayListOf(data[0]))
                            }
                        }
                    }

                    bundle.putParcelable("product", product)
                    bundle.putString("status", status)
                    listener(bundle)
                }
            }
        }
    }
}