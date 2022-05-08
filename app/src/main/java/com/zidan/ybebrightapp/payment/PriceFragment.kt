package com.zidan.ybebrightapp.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.FragmentPriceBinding
import com.zidan.ybebrightapp.model.Buy
import com.zidan.ybebrightapp.model.Price
import com.zidan.ybebrightapp.model.Product
import com.zidan.ybebrightapp.product.ProductViewModel
import com.zidan.ybebrightapp.utils.hideKeyboard
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PriceFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPriceBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPriceBinding.inflate(layoutInflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        val product = arguments?.getParcelable<Product>("product")!!
        val status = arguments?.getString("status")!!
        val price = arguments?.getParcelableArrayList<Price>("price")!!

        if (product.name == "Brightening Glow") {
            binding.tvItemPaket.visibility = View.VISIBLE
            binding.tvItemPaket.text = "Toner + Facial Wash + Serum Brightening + Night Cream + Day Cream"
        } else if (product.name == "Acne Glow") {
            binding.tvItemPaket.visibility = View.VISIBLE
            binding.tvItemPaket.text = "Facial Wash Acne + Serum Acne + Night Cream + Day Cream"
        }

        Glide.with(this).load(product.image).into(binding.imgProduct)

        val adapter = PriceAdapter(price, product.isPaket)

        binding.rvJawa.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.btnBeli.setOnClickListener {
            addToChart(product, price, status)
        }

        return binding.root
    }

    private fun addToChart(product: Product, price: ArrayList<Price>, status: String) {
        binding.btnBeli.visibility = View.GONE
        binding.layoutAdd.visibility = View.VISIBLE

        binding.btnAddChart.setOnClickListener {
            hideKeyboard()
            val qty = binding.edtQty.text.toString().trim()
            var finalPrice = 0

            if (qty.isEmpty()) {
                binding.edtQty.error = "Masukkan Jumlah"
                return@setOnClickListener
            }

            if (status == "Agen Tunggal") {
                finalPrice = price[price.lastIndex].harga
            } else {
                for (i in price) {
                    if (i.qty.contains("-")) {
                        val range = toRange(i.qty)
                        if (qty.toInt() in range) {
                            finalPrice = i.harga
                            break
                        }
                    } else {
                        finalPrice = i.harga
                    }
                }
            }

            if (!product.isPaket) {
                finalPrice = price[0].harga
            }

            val item = Buy(product.name, product.image, qty.toInt(), finalPrice, finalPrice*qty.toInt(), product.weight, product.isPaket)

            viewModel.addItem(item, status)
            dismiss()
        }
    }

    private fun toRange(string: String): IntRange {
       return string.split("-").let { (a, b) -> a.toInt()..b.toInt() }
    }
}