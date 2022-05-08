package com.zidan.ybebrightapp.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zidan.ybebrightapp.databinding.FragmentOrderBinding
import com.zidan.ybebrightapp.order.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        val id = arguments?.getString("id")

        val names = listOf("Penjualan", "Pembelian", "Stok Barang")

        if (id != null) {
            binding.pager.adapter = TabAdapter(requireActivity(), id)
            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, pos ->
                tab.text = names[pos]
            }.attach()
        } else {
            binding.mainLayout.visibility = View.GONE
            binding.errorLayout.visibility = View.VISIBLE
        }

        return binding.root
    }
}
