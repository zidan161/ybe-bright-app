package com.example.ybebrightapp.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ybebrightapp.R
import com.example.ybebrightapp.checkout.CheckoutFragment
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.databinding.FragmentProductBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Price
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.payment.PriceFragment
import com.example.ybebrightapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.*

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)

        val member = arguments?.getParcelable<Agent>(MainActivity.DATA_KEY)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        val name = resources.getStringArray(R.array.product_name)
        val image = resources.obtainTypedArray(R.array.product_image)

        val products = mutableListOf<Product>()

        for (i in name.indices) {
            val isPaket = i in 0..1
            products.add(Product(name[i], image.getResourceId(i, 0), isPaket))
        }

        image.recycle()

        binding.rvProduct.apply {
            var status = "customer"
            if (member != null) {
                status = member.keagenan
            }
            adapter = ProductAdapter(products, status) { product, stats ->
                CoroutineScope(Dispatchers.Main).launch {
                    val data = getProduct(product, stats)
                    getPrice(data)
                }
            }
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.listItem.observe(viewLifecycleOwner) {
            binding.tvItemCount.visibility = View.VISIBLE
            binding.tvItemCount.text = it.size.toString()
        }

        binding.imageButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("data", member)
            val frag = CheckoutFragment()
            frag.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.payment_frame, frag)
                ?.addToBackStack(null)
                ?.commit()
        }

        return binding.root
    }

    private suspend fun getProduct(product: Product, status: String?) =
        withContext(Dispatchers.IO) {
            val helper = JsonHelper(requireContext())
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
            bundle
        }

    private fun getPrice(bundle: Bundle) {

        val fragment = PriceFragment()
        fragment.arguments = bundle

        fragment.show(childFragmentManager, "produk")
    }
}