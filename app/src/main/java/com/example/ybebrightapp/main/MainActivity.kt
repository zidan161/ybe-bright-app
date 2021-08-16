package com.example.ybebrightapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityMainBinding
import com.example.ybebrightapp.hidok.HiDokActivity
import com.example.ybebrightapp.mainfragment.HomeFragment
import com.example.ybebrightapp.mainfragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val DATA_KEY = "data_key"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: Agent? = intent.getParcelableExtra(DATA_KEY)

        supportFragmentManager.beginTransaction().add(
            R.id.main_frame,
            HomeFragment()
        ).commit()

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, HomeFragment())
                        .commit()
                    true
                } R.id.profile -> {
                    val profileFragment = ProfileFragment()
                    if (data != null) {
                        val bundle = Bundle()
                        bundle.putParcelable(DATA_KEY, data)
                        profileFragment.arguments = bundle
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_frame,
                            profileFragment)
                        .commit()
                    true
                } else -> {
                val intent = Intent(this, HiDokActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
                true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}