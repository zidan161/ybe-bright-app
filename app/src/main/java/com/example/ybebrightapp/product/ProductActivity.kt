package com.example.ybebrightapp.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ybebrightapp.databinding.ActivityProductBinding
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        val adapter = ProductAdapter(this, viewModel.getProduct())

        binding.rvProduct.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(this@ProductActivity, 2)
        }
    }
}