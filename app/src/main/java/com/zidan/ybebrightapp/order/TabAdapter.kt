package com.zidan.ybebrightapp.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragment: FragmentActivity, private val id: String): FragmentStateAdapter(fragment) {

    private val frags = listOf(PenjualanFragment(), PembelianFragment(), StockFragment())

    override fun getItemCount(): Int = frags.size

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("id", id)

        val fragment = frags[position]
        fragment.arguments = bundle
        return fragment
    }
}