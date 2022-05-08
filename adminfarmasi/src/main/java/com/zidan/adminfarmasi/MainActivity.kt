package com.zidan.adminfarmasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zidan.adminfarmasi.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    companion object {
        const val LAST_CUST = "last_chat"
        const val LAST_ADMIN = "last_admin"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.viewpager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = when(position) {
                0 -> "Chat Customer"
                else -> "Chat Admin"
            }
        }.attach()
    }
}