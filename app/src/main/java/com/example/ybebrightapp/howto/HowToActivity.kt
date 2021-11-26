package com.example.ybebrightapp.howto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.databinding.ActivityHowtoBinding
import com.example.ybebrightapp.ingredients.IngredientAdapter
import com.example.ybebrightapp.ingredients.IngredientsViewModel
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class HowToActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHowtoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowtoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[HowToViewModel::class.java]

        val data = viewModel.getHowTo()

        val adapter = HowtoAdapter(data)

        binding.rvHowto.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@HowToActivity)
        }
    }
}