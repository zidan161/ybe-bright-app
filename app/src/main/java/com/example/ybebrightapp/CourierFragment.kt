package com.example.ybebrightapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.checkout.CheckoutViewModel
import com.example.ybebrightapp.databinding.FragmentCourierBinding
import com.example.ybebrightapp.model.Costs
import com.example.ybebrightapp.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CourierFragment : Fragment() {

    private lateinit var binding: FragmentCourierBinding
    private lateinit var viewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCourierBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityId = arguments?.getString("destination")

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[CheckoutViewModel::class.java]

        val jne = getService(cityId!!, "jne")
        val pos = getService(cityId, "pos")
        val tiki = getService(cityId, "tiki")

        jne.observe(viewLifecycleOwner) {
            binding.rvJne.apply {
                adapter = CourierAdapter(it) {
                    viewModel.setOngkir(it)
                    onDetach()
                }
                layoutManager = LinearLayoutManager(context)
            }
        }

        pos.observe(viewLifecycleOwner) {
            binding.rvPos.apply {
                adapter = CourierAdapter(it) {
                    viewModel.setOngkir(it)
                }
                layoutManager = LinearLayoutManager(context)
            }
        }

        tiki.observe(viewLifecycleOwner) {
            binding.rvTiki.apply {
                adapter = CourierAdapter(it) {
                    viewModel.setOngkir(it)
                }
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun getService(cityId: String, courier: String): LiveData<List<Costs>> {
        val data = JSONObject()
        data.put("origin", "455")
        data.put("destination", cityId)
        data.put("weight", 1)
        data.put("courier", courier)

        val json = data.toString()

        val request = json.toRequestBody("application/json".toMediaTypeOrNull())
        return viewModel.getCosts(request)
    }
}