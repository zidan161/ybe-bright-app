package com.zidan.ybebrightapp.mainpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zidan.ybebrightapp.databinding.FragmentInfoBinding
import com.zidan.ybebrightapp.info.*

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(layoutInflater)

        binding.btnSistemKeagenan.setOnClickListener {
            val intent = Intent(requireContext(), SistemAgenActivity::class.java)
            startActivity(intent)
        }

        binding.btnPeraturan.setOnClickListener {
            val intent = Intent(requireContext(), PeraturanSanksiActivity::class.java)
            startActivity(intent)
        }

        binding.btnHarga.setOnClickListener {
            val intent = Intent(requireContext(), HargaProdukActivity::class.java)
            startActivity(intent)
        }

        binding.btnKetentuanKeagenan.setOnClickListener {
            val intent = Intent(requireContext(), KetentuanAgenActivity::class.java)
            startActivity(intent)
        }

        binding.btnReward.setOnClickListener {
            val intent = Intent(requireContext(), PoinRewardActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}