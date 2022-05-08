package com.zidan.ybebrightapp.order

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.FragmentStockBinding
import com.zidan.ybebrightapp.model.Stock

class StockFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: FragmentStockBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var stockAdapter: StockAdapter

    private val viewModel: TransactionViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStockBinding.inflate(layoutInflater, container, false)

        val id = arguments?.getString("id")

        preferences = requireContext().getSharedPreferences("myShared", AppCompatActivity.MODE_PRIVATE)

        val name = resources.getStringArray(R.array.product_name)
        val image = resources.obtainTypedArray(R.array.product_image)

        val stocks = mutableListOf<Stock>()

        stockAdapter = StockAdapter(stocks)

        viewModel.getTrigger().observe(viewLifecycleOwner) {
            for (i in name.indices) {
                val qtyStock = preferences.getInt("$id${name[i]}", 0)
                val stock = Stock(name[i], image.getResourceId(i, 0), qtyStock)
                stocks.add(stock)
            }

            image.recycle()
            stockAdapter.notifyDataSetChanged()
        }

        binding.rvStock.apply {
            adapter = stockAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences, key: String?) {
        if (key != null) {
            stockAdapter.updateData(key, pref.getInt(key, 0))
        }
    }
}