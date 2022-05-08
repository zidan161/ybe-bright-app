package com.zidan.ybebrightapp.agent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.RegisterActivity
import com.zidan.ybebrightapp.databinding.ActivityAgentBinding
import com.zidan.ybebrightapp.viewmodel.ViewModelFactory

class AgentActivity : AppCompatActivity() { 

    private lateinit var binding: ActivityAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AgentViewModel::class.java]

        viewModel.getAllMember()

        val allAgents = viewModel.getAllMember()

        val pureCity = mutableListOf<String>()
        pureCity.add("Pilih Kota")

        allAgents.observe(this) {

            if (it != null) {
                val adapter = AgentAdapter(it)
                binding.rvAgent.adapter = adapter
                binding.rvAgent.layoutManager = LinearLayoutManager(this@AgentActivity)

                binding.searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        adapter.filter.filter(query)
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        adapter.filter.filter(query)
                        return true
                    }
                })
            }
        }

        binding.btnDaftarAgen.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}