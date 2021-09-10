package com.example.ybebrightapp.payment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ybebrightapp.databinding.AddItemLayoutBinding
import com.example.ybebrightapp.databinding.FragmentPriceBinding
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.model.Price
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.product.ProductActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PriceFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPriceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPriceBinding.inflate(layoutInflater, container, false)

        val product = arguments?.getParcelable<Product>("product")!!
        val status = arguments?.getString("status")!!
        val price = arguments?.getParcelableArrayList<Price>("price")!!

        Glide.with(this).load(product.image).into(binding.imgProduct)

        val jawaAdapter = PriceAdapter(price, true)
        val luarJawaAdapter = PriceAdapter(price, false)

        binding.rvJawa.apply {
            adapter = jawaAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.btnBeli.setOnClickListener {
            addToChart(true, product, price, status)
        }

        return binding.root
    }

    private fun addToChart(isJawa: Boolean, product: Product, price: ArrayList<Price>, status: String) {
        val bind = AddItemLayoutBinding.inflate(layoutInflater)
        bind.tvName.text = product.name
        Glide.with(bind.root).load(product.image).into(bind.imgProduct)

        val alert = AlertDialog.Builder(context)
            .setView(bind.root)
            .setCancelable(true)
            .create()

        alert.show()

        bind.btnAdd.setOnClickListener {
            val qty = bind.edtQty.text.toString().trim().toInt()
            var finalPrice = 0

            if (status == "agen tunggal") {
                finalPrice = if (isJawa) {
                    price[price.lastIndex].jawaBali
                } else {
                    price[price.lastIndex].luarJawaBali
                }
            } else {
                for (i in price) {
                    val range = toRange(i.qty)
                    if (qty in range) {
                        if (isJawa) {
                            finalPrice = i.jawaBali
                            break
                        } else {
                            finalPrice = i.luarJawaBali
                            break
                        }
                    }
                }
            }

            val item = Buy(product.name, product.image, qty, finalPrice, finalPrice*qty)

            val act = activity as ProductActivity
            act.addCheckoutItem(item)
            alert.hide()
        }
    }

    private fun toRange(string: String): IntRange {
       return string.split("-").let { (a, b) -> a.toInt()..b.toInt() }
    }
}