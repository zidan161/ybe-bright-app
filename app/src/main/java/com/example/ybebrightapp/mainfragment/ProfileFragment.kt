package com.example.ybebrightapp.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.databinding.FragmentProfileBinding
import com.example.ybebrightapp.main.MainActivity

class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val data = arguments?.getParcelable<Agent>(MainActivity.DATA_KEY)

            if (data != null) {
                with(fragmentProfileBinding) {
                    tvName.text = data.name
                    tvValId.text = data.id
                    tvValPoin.text = data.poin.toString()
                    tvNik.text = data.nik
                    tvPhone.text = data.phone
                    tvEmail.text = data.email
                    tvAddress.text = "${data.address}, ${data.city}"
                }
            } else {

            }
        }
    }
}