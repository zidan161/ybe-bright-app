package com.example.ybebrightapp.checkout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ybebrightapp.databinding.SetAddressBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.CourierFragment
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.FragmentCheckoutBinding
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Order
import com.example.ybebrightapp.product.ProductViewModel
import com.example.ybebrightapp.utils.*
import com.example.ybebrightapp.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Math.random

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var proAdapter: CheckoutAdapter

    private var cityId = ""
    private lateinit var loading: AlertDialog
    private lateinit var fragment: CourierFragment

    private var courier: String? = null
    private var cost: Int? = null
    private var status = "customer"

    private var address = ""
    private var city = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckoutBinding.inflate(layoutInflater, container, false)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_PHONE_STATE), 101);
        }

        val member = arguments?.getParcelable<Agent>("data")

        if (member != null) {
            status = member.status
            binding.edtName.setText(member.name)
            binding.edtPhone.setText(member.phone)
        }

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        loading = showLoading()

        proAdapter = CheckoutAdapter { pos ->
            viewModel.removeItem(pos)
        }

        binding.recyclerView.apply {
            adapter = proAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        var harga = 0
        viewModel.listItem.observe(viewLifecycleOwner) { data ->

            if (data != null) {
                proAdapter.setData(data)

                for (i in data) {
                    harga += i.total
                }
                binding.tvHarga.text = harga.setDecimal()

                binding.btnSetAddress.setOnClickListener {
                    loading.show()
                    setAddress()
                }

                binding.btnSetCourier.setOnClickListener { openCourier() }

            } else {
                binding.btnSetAddress.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Tidak ada item di keranjang",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.btnSetCourier.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Tidak ada item di keranjang",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.address.observe(viewLifecycleOwner) {
            binding.tvAddress.text = it
        }

        viewModel.ongkir.observe(viewLifecycleOwner) {
            courier = it.service
            cost = it.costs[0].cost

            binding.tvOngkir.text = it.costs[0].cost.setDecimal()
            binding.tvCourier.text = "$courier ${it.costs[0].cost.setDecimal()}"
            val total = (cost!! + harga).setDecimal()
            binding.tvTotal.text = "Total: $total"
            activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
        }

        binding.btnBeli.setOnClickListener {
            val name = binding.edtName.text.toString()
            val phone = binding.edtPhone.text.toString().trim()

            if (name.isEmpty()) {
                binding.edtName.error = "Field ini harus diisi!"
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.edtPhone.error = "Field ini harus diisi!"
                return@setOnClickListener
            }
            if (courier == null) {
                return@setOnClickListener
            }
            if (cost == null) {
                return@setOnClickListener
            }

            val upload = Order(name, status,
                getDate(),
                phone,
                viewModel.address.value,
                viewModel.listItem.value,
                courier,
                cost, false)

            val db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
            db.getReference("order").push().setValue(upload).addOnSuccessListener {
                Snackbar.make(binding.root, "Order sudah dibuat", Snackbar.LENGTH_SHORT).show()
            }

            val midtrans = Midtrans.getInstance(requireContext())
            var amount = 0
            amount += upload.cost!!
            upload.buy?.forEach {
                amount += it.total
            }
            val id = (1..1000).random().toString().padStart(4, '0')
            midtrans.createTransaction(
                "${member?.id ?: "CSM"}$id",
                amount,
                upload,
                address,
                city,
                viewModel.courier
            )
        }
        return binding.root
    }

    private fun setAddress(){
        val bind = SetAddressBinding.inflate(layoutInflater)

        val province = viewModel.getProvinces()
        val dialog = showAlert(requireContext(), bind.root, false)

        province.observe(viewLifecycleOwner) {
            loading.hide()
            val adapter = ProvinceAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtProvince.setAdapter(adapter)

            dialog.show()
            bind.btnOk.setOnClickListener {
                val pro = bind.edtProvince.text.toString()
                city = bind.edtCity.text.toString()
                address = bind.edtAddress.text.toString()

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

                viewModel.address.value = "$address, $city, $pro"
                hideKeyboard()
                dialog.dismiss()
            }
        }

        bind.edtProvince.setOnItemClickListener { _, _, _, id ->
            viewModel.setId(id.toString())
            dialog.hide()
            loading.show()
        }

        viewModel.cities.observe(viewLifecycleOwner) {
            loading.hide()
            dialog.show()
            val adapter = CityAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
            bind.edtCity.setAdapter(adapter)
        }

        bind.edtCity.setOnItemClickListener { _, _, _, id ->
            cityId = id.toString()
        }
    }

    private fun openCourier() {
        val bundle = Bundle()
        bundle.putString("destination", cityId)
        fragment = CourierFragment()
        fragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.payment_frame, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}