package com.example.ybebrightapp.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityProductBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Agent>("data")

        val fragment = ProductFragment()
        if (data != null) {
            val bundle = Bundle()
            bundle.putParcelable(MainActivity.DATA_KEY, data)
            fragment.arguments = bundle
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.payment_frame, fragment)
            .commit()
    }
}