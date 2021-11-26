package com.example.ybebrightapp.agent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.RegisterActivity
import com.example.ybebrightapp.databinding.ActivityAgentBinding
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class AgentActivity : AppCompatActivity() { 

    private lateinit var binding: ActivityAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AgentViewModel::class.java]

        val allAgents = viewModel.getAllMember()

        allAgents.observe(this) {

            if (it != null) {
                val adapter = AgentAdapter(it)
                binding.rvAgent.apply {
                    this.adapter = adapter
                    layoutManager = LinearLayoutManager(this@AgentActivity)
                }
            }
        }

        binding.btnDaftarAgen.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}