package com.example.ybebrightapp.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ybebrightapp.databinding.ActivityCheckoutBinding
import com.example.ybebrightapp.databinding.SetAddressBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.CourierFragment
import com.example.ybebrightapp.R
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.utils.setDecimal
import com.example.ybebrightapp.utils.showAlert
import com.example.ybebrightapp.utils.showLoading
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var viewModel: CheckoutViewModel

    private var cityId = ""
    private lateinit var loading: AlertDialog
    private lateinit var fragment: CourierFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[CheckoutViewModel::class.java]

        val data = intent.getParcelableArrayListExtra<Buy>("data")

        loading = showLoading()

        var harga = 0
        if (data != null) {
            val adapter = CheckoutAdapter(data)

            for (i in data) {
                harga += i.total
            }
            binding.tvHarga.text = harga.setDecimal()

            binding.recyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(this@CheckoutActivity)
            }

            binding.btnSetAddress.setOnClickListener {
                loading.show()
                setAddress()
            }

            binding.btnSetCourier.setOnClickListener {
                openCourier()
            }

            viewModel.ongkir.observe(this) {
                binding.tvOngkir.text = it.costs[0].cost.setDecimal()
                binding.tvCourier.text = "${it.service} ${it.costs[0].cost.setDecimal()}"
                val total = (it.costs[0].cost + harga).setDecimal()
                binding.tvTotal.text = "Total: $total"
                supportFragmentManager.beginTransaction().remove(fragment).commit()
                setBack()
            }
        } else {
            binding.btnSetAddress.setOnClickListener {
                Toast.makeText(this, "Tidak ada item di keranjang", Toast.LENGTH_SHORT).show()
            }

            binding.btnSetCourier.setOnClickListener {
                Toast.makeText(this, "Tidak ada item di keranjang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAddress(){
        val bind = SetAddressBinding.inflate(layoutInflater)

        val province = viewModel.getProvinces()
        val dialog = showAlert(this, bind.root, false)

        province.observe(this) {
            loading.hide()
            val adapter = ProvinceAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtProvince.setAdapter(adapter)

            dialog.show()
            bind.btnOk.setOnClickListener {
                val pro = bind.edtProvince.text.toString()
                val city = bind.edtCity.text.toString()
                val address = bind.edtAddress.text.toString()

                if (pro.isEmpty()) {
                    bind.edtProvince.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (city.isEmpty()) {
                    bind.edtCity.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }
                if (address.isEmpty()) {
                    bind.edtAddress.error = "Field ini harus diisi!"
                    return@setOnClickListener
                }

                binding.tvAddress.text = "$address, $city, $pro"
                dialog.dismiss()
            }
        }

        bind.edtProvince.setOnItemClickListener { _, _, _, id ->
            viewModel.setId(id.toString())
            dialog.hide()
            loading.show()
        }

        viewModel.cities.observe(this) {
            loading.hide()
            dialog.show()
            val adapter = CityAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtCity.setAdapter(adapter)
        }

        bind.edtCity.setOnItemClickListener { _, _, _, id ->
            cityId = id.toString()
        }
    }

    private fun setBack() {
        with(binding) {
            loading.hide()
            recyclerView.visibility = View.VISIBLE
            tvAddress.visibility = View.VISIBLE
            btnSetAddress.visibility = View.VISIBLE
            tvCourier.visibility = View.VISIBLE
            btnSetCourier.visibility = View.VISIBLE
            tvH1.visibility = View.VISIBLE
            tvOnk.visibility = View.VISIBLE
            tvOngkir.visibility = View.VISIBLE
            tvHar.visibility = View.VISIBLE
            tvHarga.visibility = View.VISIBLE
            tvTotal.visibility = View.VISIBLE
            shadow.visibility = View.VISIBLE
            btnBeli.visibility = View.VISIBLE
        }
    }

    private fun openCourier() {
        with(binding) {
            loading.show()
            recyclerView.visibility = View.GONE
            tvAddress.visibility = View.GONE
            btnSetAddress.visibility = View.GONE
            tvCourier.visibility = View.GONE
            btnSetCourier.visibility = View.GONE
            tvH1.visibility = View.GONE
            tvOnk.visibility = View.GONE
            tvOngkir.visibility = View.GONE
            tvHar.visibility = View.GONE
            tvHarga.visibility = View.GONE
            tvTotal.visibility = View.GONE
            shadow.visibility = View.GONE
            btnBeli.visibility = View.GONE

            val bundle = Bundle()
            bundle.putString("destination", cityId)
            fragment = CourierFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_checkout, fragment).commit()
        }
    }
}