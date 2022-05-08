package com.zidan.ybebrightapp.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zidan.ybebrightapp.R
import com.zidan.ybebrightapp.databinding.ActivityProductBinding
import com.zidan.ybebrightapp.main.MainActivity
import com.zidan.ybebrightapp.model.Agent

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