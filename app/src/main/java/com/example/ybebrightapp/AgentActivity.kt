package com.example.ybebrightapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ybebrightapp.databinding.ActivityAgentBinding

class AgentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}