package com.example.ybebrightapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityMainBinding
import com.example.ybebrightapp.databinding.DialogImageBinding
import com.example.ybebrightapp.hidok.ConsulActivity
import com.example.ybebrightapp.mainpage.HomeFragment
import com.example.ybebrightapp.mainpage.ProfileFragment
import com.example.ybebrightapp.utils.showAlert

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

        createAlert(R.drawable.pop_up)

        val fragment = HomeFragment()
        if (data != null) {
            val bundle = Bundle()
            bundle.putParcelable(DATA_KEY, data)
            fragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction().add(
            R.id.main_frame, fragment
        ).commit()

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
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
                    val intent = Intent(this, ConsulActivity::class.java)
                    intent.putExtra("data", data)
                    startActivity(intent)
                    true
                }
            }
        }
    }

    private fun createAlert(img: Int) {
        val binding = DialogImageBinding.inflate(layoutInflater)
        Glide.with(this).load(img).into(binding.imgView)
        val alert = showAlert(this, binding.root, true)
        alert.show()
        binding.btnClose.setOnClickListener {
            alert.cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}