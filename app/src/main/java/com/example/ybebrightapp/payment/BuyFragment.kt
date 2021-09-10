package com.example.ybebrightapp.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.ybebrightapp.databinding.FragmentBuyBinding
import com.example.ybebrightapp.model.Price

class BuyFragment : Fragment() {

    private lateinit var binding: FragmentBuyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentBuyBinding.inflate(layoutInflater, container, false)

        val data = arguments?.getParcelableArrayList<Price>("data")!!
        val isJawa = arguments?.getBoolean("jawa")!!

        binding.edtJumlah.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().toInt().equals(1..4)) {
                if (isJawa) {
                    data[0].jawaBali
                } else {
                    data[0].luarJawaBali
                }
            }
        }

        binding.btnAdd.setOnClickListener {

        }

        return binding.root
    }
}