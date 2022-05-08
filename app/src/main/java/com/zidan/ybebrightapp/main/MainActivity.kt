package com.zidan.ybebrightapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.ActivityMainBinding
import com.zidan.ybebrightapp.databinding.DialogImageBinding
import com.zidan.ybebrightapp.hidok.ConsulActivity
import com.zidan.ybebrightapp.login.LoginActivity
import com.zidan.ybebrightapp.mainpage.HomeFragment
import com.zidan.ybebrightapp.mainpage.InfoFragment
import com.zidan.ybebrightapp.mainpage.OrderFragment
import com.zidan.ybebrightapp.mainpage.ProfileFragment
import com.zidan.ybebrightapp.utils.showAlert

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
                            profileFragment, ProfileFragment::class.java.simpleName)
                        .commit()
                    true
                } R.id.info -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_frame,
                            InfoFragment()
                        )
                        .commit()
                    true
                } R.id.order -> {
                val fragmentOrder = OrderFragment()
                if (data != null) {
                    val bundle = Bundle()
                    bundle.putString("id", data.idMember)
                    fragmentOrder.arguments = bundle
                }
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_frame,
                        fragmentOrder)
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

    fun finishSetAddress() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logout() {
        wantToOut("logout")
    }

    override fun onBackPressed() {
        wantToOut("keluar")
    }

    private fun wantToOut(request: String) {
        AlertDialog.Builder(this)
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setNegativeButton("Tidak") { it, _ -> it.dismiss() }
            .setPositiveButton("Ya") { _, _ ->
                if (request == "keluar") {
                    super.onBackPressed()
                } else {
                    finishSetAddress()
                }
            }.create().show()
    }
}