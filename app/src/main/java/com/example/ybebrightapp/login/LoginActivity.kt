package com.example.ybebrightapp.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.RegisterActivity
import com.example.ybebrightapp.agent.AgentViewModel
import com.example.ybebrightapp.databinding.ActivityLoginBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.utils.hideKeyboard
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AgentViewModel::class.java]

        preferences = getSharedPreferences("myShared", MODE_PRIVATE)

        val idSimpan = preferences.getString("id", null)

        var fixedData: Agent? = null

        val id = MutableLiveData<String>()

        binding.btnSkip.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.edtId.setOnFocusChangeListener { _, hasFocus ->
            binding.imgCheck.visibility = View.GONE
            if (!hasFocus) {
                val idText = binding.edtId.text.toString()
                if (idSimpan != null) {
                    if (idText == idSimpan) {
                        binding.edtId.error = "Aplikasi hanya boleh satu akun"
                        return@setOnFocusChangeListener
                    }
                }
                binding.pgId.visibility = View.VISIBLE

                id.value = idText
                println(id.value)
            }
        }

        id.observe(this) { idMember ->

            val data = viewModel.getMember(idMember)

            data.observe(this) { member ->
                if (member == null) {
                    binding.pgId.visibility = View.GONE
                    binding.edtId.error = "ID tidak terdaftar"
                } else {
                    fixedData = member
                    if (idSimpan == null) {
                        preferences.edit().putString("id", member.idMember).apply()
                    }
                    binding.pgId.visibility = View.GONE
                    binding.imgCheck.visibility = View.VISIBLE
                    binding.edtId.error = null
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            if (fixedData != null) {
                if (binding.edtPhone.text.toString() == fixedData?.no_hp) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(MainActivity.DATA_KEY, fixedData)
                    startActivity(intent)
                    finish()
                } else {
                    binding.edtPhone.error = "Nomor telepon tidak sesuai!"
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}