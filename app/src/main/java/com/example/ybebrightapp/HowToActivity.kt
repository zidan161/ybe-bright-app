package com.example.ybebrightapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ybebrightapp.databinding.ActivityHowtoBinding

class HowToActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHowtoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowtoBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}