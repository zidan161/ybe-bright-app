package com.example.ybebrightapp.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.FragmentProductBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.payment.PriceFragment

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)

        val member = arguments?.getParcelable<Agent>(MainActivity.DATA_KEY)

        val name = resources.getStringArray(R.array.product_name)
        val image = resources.obtainTypedArray(R.array.product_image)

        val products = mutableListOf<Product>()

        for (i in name.indices) {
            var isPaket = false
            if (i in 0..1){
                isPaket = true
            }
            products.add(Product(name[i], image.getResourceId(i, 0), isPaket))
        }

        image.recycle()

        binding.rvProduct.apply {
            var status = "customer"
            if (member != null) {
                status = member.status
            }
            adapter = ProductAdapter(requireContext(), products, status) { getPrice(it) }
            layoutManager = GridLayoutManager(context, 2)
        }

        return binding.root
    }

    private fun getPrice(bundle: Bundle) {

        val fragment = PriceFragment()
        fragment.arguments = bundle

        fragment.show(childFragmentManager, "produk")
    }
}