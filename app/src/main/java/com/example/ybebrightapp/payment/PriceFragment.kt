package com.example.ybebrightapp.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.FragmentPriceBinding
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.model.Price
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.product.ProductViewModel
import com.example.ybebrightapp.utils.hideKeyboard
import com.example.ybebrightapp.viewmodel.ViewModelFactory
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

        Glide.with(this).load(product.image).into(binding.imgProduct)

        val adapter = PriceAdapter(price)

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

            val minimalOrder: Int =
                if (status == "Agen Tunggal" || status == "Agen") { 10 }
                else if (status == "Agen Tunggal") { 3 }
                else { 0 }

            if (status == "Agen Tunggal") {
                finalPrice = price[price.lastIndex].harga
            } else {
                for (i in price) {
                    val range = toRange(i.qty)
                    if (qty.toInt() in range) {
                        finalPrice = i.harga
                        break
                    } else if (qty.toInt() > range.last) {
                        finalPrice = price[price.lastIndex].harga
                    }
                }
            }

            if (!product.isPaket) {
                if (qty.toInt() != minimalOrder) {
                    binding.edtQty.error = "Jumlah kurang dari batas minimal"
                    return@setOnClickListener
                }
            }

            val item = Buy(product.name, product.image, qty.toInt(), finalPrice, finalPrice*qty.toInt(), product.isPaket)

            viewModel.addItem(item)
            dismiss()
        }
    }

    private fun toRange(string: String): IntRange {
       return string.split("-").let { (a, b) -> a.toInt()..b.toInt() }
    }
}