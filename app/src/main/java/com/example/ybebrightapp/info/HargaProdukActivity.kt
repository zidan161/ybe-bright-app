package com.example.ybebrightapp.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.databinding.ActivityHargaProdukBinding

class HargaProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHargaProdukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHargaProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val helper = JsonHelper(this)

        val dataAgent = helper.loadPriceByStatus("Agen Tunggal")
        val dataAgen = helper.loadPriceByStatus("Agen")
        val dataReseller = helper.loadPriceByStatus("Reseller")
        val dataCustomer = helper.loadPriceByStatus("customer")

        val listData = listOf(dataAgent, dataAgen, dataReseller, dataCustomer)

        val adapter = HargaProdukAdapter(listData)

        binding.rvHarga.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@HargaProdukActivity)
        }
    }
}