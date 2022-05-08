package com.zidan.ybebrightapp.checkout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.CourierFragment
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.*
import com.zidan.ybebrightapp.model.*
import com.zidan.ybebrightapp.product.ProductViewModel
import com.zidan.ybebrightapp.utils.*
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.zidan.ybebrightapp.model.Agent

class CheckoutFragment : Fragment(), TransactionFinishedCallback {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var proAdapter: CheckoutAdapter

    private var cityId = ""
    private lateinit var loading: AlertDialog
    private lateinit var fragment: CourierFragment
    private var member: Agent? = null

    private var courier: String? = null
    private var cost: Int? = null
    private var status = "customer"
    private var weight = 0
    private lateinit var upload: Order

    private var address = ""
    private var city = ""
    private var isAfterCourier = false
    private lateinit var list: List<Buy>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckoutBinding.inflate(layoutInflater, container, false)

        loading = showLoading()

        member = arguments?.getParcelable("data")

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        if (member != null) {
            status = member!!.keagenan
            binding.edtName.setText(member!!.nama)
            binding.edtPhone.setText(member!!.no_hp)
            binding.tvAddress.text = "${member!!.alamat}, ${member!!.kota}, ${member!!.provinsi}"
            setAddress()
        }

        member?.idMember

        val minimalPaket = when (member?.keagenan) {
            "Reseller" -> 3
            "Agen" -> 25
            "Agen Tunggal" -> 100
            else -> 1
        }

        val minimal: Int =
            if (status == "Agen Tunggal" || status == "Agen") { 10 }
            else if (status == "Reseller") { 3 }
            else { 1 }

        proAdapter = CheckoutAdapter { list, item ->
            viewModel.listItem.value = list as MutableList<Buy>
            if (list.isNotEmpty()) {
                viewModel.reCount(item, status, false)
            }
        }

        binding.recyclerView.apply {
            adapter = proAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        var harga = 0
        viewModel.listItem.observe(viewLifecycleOwner) { data ->

            var countPaket = 0
            var countSatuan = 0
            var containPaket = false
            var containSatuan = false

            harga = 0

            if (data.isNotEmpty()) {

                list = data
                data.forEach {
                    if (it.isPaket) {
                        countPaket += it.count
                        containPaket = true
                    } else if (!it.isPaket) {
                        countSatuan += it.count
                        containSatuan = true
                    }
                    harga += it.total
                    // Menghitung berat
                    weight += it.count * it.weight
                }

                proAdapter.setData(data)

                if (containPaket && countPaket < minimalPaket) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "Jumlah Paket tidak sesuai dengan batas minimal"
                    return@observe
                }

                if (containSatuan && countSatuan < minimal) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "Jumlah item tidak sesuai dengan batas minimal"
                    return@observe
                }

                binding.tvAddress.setOnClickListener {
                    loading.show()
                    setAddressNonMember()
                }

                binding.tvCourier.setOnClickListener { openCourier() }
            } else {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = "Tidak ada item di keranjang"
            }
            binding.tvHarga.text = harga.setDecimal()
        }

        viewModel.address.observe(viewLifecycleOwner) {
            binding.tvAddress.text = it
        }

        viewModel.ongkir.observe(viewLifecycleOwner) {
            if (status == "Agen Tunggal") {
                courier = it.service
                cost = 0
                binding.tvOngkir.text = "Gratis"
                binding.tvCourier.text = "$courier Gratis"
            } else {
                courier = it.service
                cost = it.costs[0].cost

                binding.tvOngkir.text = it.costs[0].cost.setDecimal()
                binding.tvCourier.text = "$courier ${it.costs[0].cost.setDecimal()}"
                val total = (cost!! + harga).setDecimal()
                binding.tvTotal.text = "Total: $total"
            }
            if (isAfterCourier) {
                activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
            }
        }

        binding.btnBeli.setOnClickListener {
            if (list.isEmpty()) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = "Tidak ada item di keranjang"
                return@setOnClickListener
            }
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

            dialogPayment(name, phone)
        }
        return binding.root
    }

    private fun doPayment(amount: Int, id: String) {
        //Request to Midtrans
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        } else {
            val midtrans = Midtrans.getInstance(requireContext(), this)
            midtrans.createTransaction(
                id,
                amount,
                upload,
                address,
                city,
                viewModel.courier,
            )
        }
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    uploadDataBuy(result.response.orderId)
                }
            }
        }
    }

    private fun uploadDataBuy(orderId: String) {
        val db =
            Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
        db.getReference("order").push().setValue(upload).addOnSuccessListener {
            Snackbar.make(binding.root, "Order sudah dibuat, dengan ORDER ID: $orderId", Snackbar.LENGTH_SHORT)
                .show()
            viewModel.listItem.value = mutableListOf()
            activity?.onBackPressed()
        }
    }

    private fun setAddressNonMember() {
        val bind = SetAddressBinding.inflate(layoutInflater)

        val (province, error) = viewModel.getProvinces()
        val dialog = showAlert(requireContext(), bind.root, false)

        bind.btnCancel.setOnClickListener {
            dialog.cancel()
        }

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

    private fun setAddress() {
        loading.show()
        val (cities, error) = viewModel.getAllCity()

        cities.observe(viewLifecycleOwner) {
            val split = member?.kota?.getSplit()
            val type = if (split!![0].lowercase().contains("kab")) {
                "Kabupaten"
            } else {
                "Kota"
            }

            var kota = ""
            for (j in split.indices) {
                if (j != 0) {
                    kota += split[j] + " "
                    if (j == split.lastIndex) {
                        it.find { city ->
                            city.name.lowercase() == kota.lowercase().trim() && city.type == type
                        }.let { result ->
                            if (result != null) {
                                cityId = result.id
                                loading.hide()
                            }
                        }
                        break
                    }
                }
            }
        }
    }

    private fun String.getSplit(): Array<String> {
        return this.split(" ").toTypedArray()
    }

    private fun dialogPayment(name: String, phone: String) {
        val bind = DialogPaymentBinding.inflate(layoutInflater)
        val dialogPayment = showAlert(requireContext(), bind.root, true)

        upload = Order(
            member?.idMember,
            viewModel.sellerId,
            name, status,
            getDate(),
            phone,
            viewModel.address.value,
            viewModel.listItem.value,
            courier,
            cost, false
        )

        var amount = 0
        amount += upload.cost!!
        upload.buy?.forEach {
            amount += it.total
        }
        val numberId = (1..1000).random().toString().padStart(4, '0')
        val id = "${member?.idMember ?: "CSM"}$numberId"

        bind.btnLangsung.setOnClickListener {
            doPayment(amount, id)
            dialogPayment.dismiss()
        }
        bind.btnCod.setOnClickListener {
            val v = DialogConfirmCodBinding.inflate(layoutInflater)
            val dialog = showAlert(requireContext(), v.root, false)

            v.tvHarga.text = "Jumlah yang harus dibayarkan saat menerima barang: Rp.${amount.setDecimal()}"
            v.btnOk.setOnClickListener {
                uploadDataBuy(id)
                dialog.dismiss()
            }

            dialog.show()
            dialogPayment.dismiss()
        }
        dialogPayment.show()
    }

    private fun openCourier() {
        val bundle = Bundle()
        if (member != null) bundle.putString("idRef", member?.idReference)
        bundle.putString("destination", cityId)
        bundle.putString("status", status)
        bundle.putInt("weight", weight)
        fragment = CourierFragment()
        fragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.payment_frame, fragment)
            ?.addToBackStack(null)
            ?.commit()
        isAfterCourier = true
    }
}