package com.example.ybebrightapp.ingredients

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ybebrightapp.R
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.databinding.ActivityIngredientBinding
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class IngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[IngredientsViewModel::class.java]

        val data = viewModel.getIngredient()

        val adapter = IngredientAdapter(data)

        binding.rvIngredients.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@IngredientActivity)
        }
    }
}