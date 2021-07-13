package com.example.ybebrightapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.ybebrightapp.HowToActivity
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityMainBinding
import com.example.ybebrightapp.product.ProductActivity
import com.synnapps.carouselview.ImageListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val data = listOf(R.drawable.download, R.drawable.download1, R.drawable.images)

    var imageListener = ImageListener { position, imageView ->
        imageView.setImageResource(data[position])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setLogo(R.drawable.ybebright_logo_master_lanscape_small)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)

        binding.mainCv.pageCount = data.size
        binding.mainCv.setImageListener(imageListener)

        with(binding) {
            btnPrice.setOnClickListener {
                val intent = Intent(this@MainActivity, ProductActivity::class.java)
                startActivity(intent)
            }
            btnHowto.setOnClickListener {
                val intent = Intent(this@MainActivity, HowToActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}