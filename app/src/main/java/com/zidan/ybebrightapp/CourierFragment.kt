package com.zidan.ybebrightapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.databinding.FragmentCourierBinding
import com.zidan.ybebrightapp.model.City
import com.zidan.ybebrightapp.model.Costs
import com.zidan.ybebrightapp.product.ProductViewModel
import com.zidan.ybebrightapp.utils.showLoading
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.cachapa.expandablelayout.ExpandableLayout

class CourierFragment : Fragment() {

    private lateinit var binding: FragmentCourierBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var db: FirebaseDatabase
    private lateinit var loading: AlertDialog
    private var cities = mutableListOf<City>()
    private val isLoading = MutableLiveData(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCourierBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityId = arguments?.getString("destination")
        val status = arguments?.getString("status")
        val weight = arguments?.getInt("weight")
        val idRef = arguments?.getString("idRef")

        loading = showLoading()
        loading.show()

        isLoading.observe(viewLifecycleOwner) {
            if (it) {
                loading.show()
            } else {
                loading.hide()
            }
        }

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        db = Firebase.database("https://ybebright-app-default-rtdb.asia-southeast1.firebasedatabase.app/")

        if (idRef?.trim() == "PUSAT") {
            binding.tvInfo.text = "Dikirim dari pusat"
            binding.tvPusat.visibility = View.VISIBLE
            binding.rvCabang.visibility = View.GONE
            getAllService(status!!, "399", cityId!!, weight!!)
        } else {
            val (city, error) = viewModel.getAllCity()

            city.observe(viewLifecycleOwner) {
                cities.addAll(it)
            }

            val data = viewModel.getMemberByValue(idRef)

            data.observe(viewLifecycleOwner) {

                if (it != null) {

                    isLoading.value = false

                    val cabangAdapter = KotaCabangAdapter(requireContext(), it) { agent ->
                        viewModel.sellerId = agent.idMember
                        binding.rvCabang.visibility = View.GONE
                        val split = agent.kota.getSplit()
                        val type = if (split[0].lowercase().contains("kab")) "Kabupaten" else "Kota"

                        var kota = ""
                        var origin = ""
                        for (j in split.indices) {
                            if (j != 0) {
                                kota += split[j] + " "
                                if (j == split.lastIndex) {
                                    cities.find { city -> city.name.lowercase() == kota.lowercase().trim()
                                            && city.type == type }.let { result ->
                                        if (result != null) {
                                            origin = result.id
                                        }
                                    }
                                    break
                                }
                            }
                        }
                        getAllService(status!!, origin, cityId!!, weight!!)
                    }

                    val manager = LinearLayoutManager(requireContext())
                    manager.stackFromEnd

                    binding.rvCabang.apply {
                        adapter = cabangAdapter
                        layoutManager = manager
                    }
                }
            }
        }
    }

    private fun String.getSplit(): Array<String> {
        return this.split(" ").toTypedArray()
    }

    private fun getService(origin: String, cityId: String, weight: Int, courier: String): Pair<LiveData<List<Costs>>, LiveData<String?>> {
        return viewModel.getCosts(origin, cityId, weight, courier)
    }

    private fun expand(v: ExpandableLayout) {
        if (!v.isExpanded) v.expand()
        else v.collapse()
    }

    private fun getAllService(status: String, origin: String, cityId: String, weight: Int) {
        println("$origin, $cityId, $weight")
        isLoading.value = true
        binding.layoutCourier.visibility = View.VISIBLE
        val (jne, errorJne) = getService(origin, cityId, weight, "jne")
        val (pos, errorPos) = getService(origin, cityId, weight, "pos")
        val (tiki, errorTiki) = getService(origin, cityId, weight, "tiki")

        errorJne.observe(viewLifecycleOwner) {
            if (it != null) errorLoad(it)
        }
        errorPos.observe(viewLifecycleOwner) {
            if (it != null) errorLoad(it)
        }
        errorTiki.observe(viewLifecycleOwner) {
            if (it != null) errorLoad(it)
        }

        binding.expandableJne.setOnClickListener { expand(it as ExpandableLayout) }
        binding.expandablePos.setOnClickListener { expand(it as ExpandableLayout) }
        binding.expandableTiki.setOnClickListener { expand(it as ExpandableLayout) }

        jne.observe(viewLifecycleOwner) {
            if (it != null) isLoading.value = false
            binding.rvJne.apply {
                adapter = CourierAdapter(it, status) { cost ->
                    viewModel.setOngkir(cost)
                    viewModel.courier = "JNE"
                    activity?.onBackPressed()
                }
                layoutManager = LinearLayoutManager(context)
            }
        }

        pos.observe(viewLifecycleOwner) {
            if (it != null) isLoading.value = false
            binding.rvPos.apply {
                adapter = CourierAdapter(it, status) { cost ->
                    viewModel.setOngkir(cost)
                    viewModel.courier = "POS"
                    activity?.onBackPressed()
                }
                layoutManager = LinearLayoutManager(context)
            }
        }

        tiki.observe(viewLifecycleOwner) {
            if (it != null) isLoading.value = false
            binding.rvTiki.apply {
                adapter = CourierAdapter(it, status) { cost ->
                    viewModel.setOngkir(cost)
                    viewModel.courier = "TIKI"
                    activity?.onBackPressed()
                }
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun errorLoad(err: String) {
        isLoading.value = false
        Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show()
    }
}