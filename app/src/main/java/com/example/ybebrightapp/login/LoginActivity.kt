package com.example.ybebrightapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.ybebrightapp.Agent
import com.example.ybebrightapp.agent.AgentViewModel
import com.example.ybebrightapp.databinding.ActivityLoginBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AgentViewModel::class.java]

        var fixedData: Agent? = null

        val allData = viewModel.getAllList()
        val id = MutableLiveData<Agent>()

        binding.tvSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.edtId.setOnFocusChangeListener { _, hasFocus ->
            binding.imgCheck.visibility = View.GONE
            if (!hasFocus) {

                binding.pgId.visibility = View.VISIBLE

                var count = 0
                for (i in allData) {
                    if (binding.edtId.text.toString() == i.id) {
                        id.value = i
                        break
                    }
                    count++
                    if (count == allData.size) {
                        binding.pgId.visibility = View.GONE
                        binding.edtId.error = "ID tidak terdaftar"
                    }
                }
            }
        }

        id.observe(this) { agent ->
            var isAgent = false

            val listAgent = viewModel.getListAgent(agent.poin)
            val listReseller = viewModel.getListReseller(agent.poin)
            for (i in listAgent) {
                if (i.id == agent.id) {
                    fixedData = i
                    isAgent = true
                    break
                }
            }
            if (!isAgent) {
                for (i in listReseller) {
                    if (i.id == agent.id) {
                        fixedData = i
                        break
                    }
                }
            }
            binding.pgId.visibility = View.GONE
            binding.imgCheck.visibility = View.VISIBLE
        }

        binding.btnLogin.setOnClickListener {
            if (fixedData != null) {
                if (binding.edtPhone.text.toString() == fixedData?.phone) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(MainActivity.DATA_KEY, fixedData)
                    startActivity(intent)
                } else {
                    binding.edtPhone.error = "Nomor telepon tidak sesuai!"
                }
            }
        }
    }
}