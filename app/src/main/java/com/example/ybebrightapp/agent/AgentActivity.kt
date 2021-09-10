package com.example.ybebrightapp.agent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.RegisterActivity
import com.example.ybebrightapp.databinding.ActivityAgentBinding
import com.example.ybebrightapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgentActivity : AppCompatActivity() { 

    private lateinit var binding: ActivityAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AgentViewModel::class.java]

        val allAgents = viewModel.getAllList()

        CoroutineScope(Dispatchers.Default).launch {
            for (i in allAgents) {
                if (i.nik != null) {
                    val agent = viewModel.getListAgent(i.nik, i.poin)
                    val agenTunggal = viewModel.getListAgentTunggal(i.nik, i.poin)
                    val reseller = viewModel.getListReseller(i.nik, i.poin)

                    when {
                        agent != null -> {
                            i.phone = agent.phone
                            i.address = agent.address
                            i.city = agent.city
                        }
                        agenTunggal != null -> {
                            i.phone = agenTunggal.phone
                            i.address = agenTunggal.address
                            i.city = agenTunggal.city
                        }
                        reseller != null -> {
                            i.phone = reseller.phone
                            i.address = reseller.address
                            i.city = reseller.city
                        }
                    }
                }
            }
        }

        binding.btnDaftarAgen.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val adapter = AgentAdapter(allAgents)
        binding.rvAgent.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@AgentActivity)
        }
    }
}