package com.example.adminfarmasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfarmasi.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")
        val des = intent.getStringExtra("des")

        binding.tvId.text = id
        binding.tvName.text = name
        binding.tvDescription.text = des
    }
}