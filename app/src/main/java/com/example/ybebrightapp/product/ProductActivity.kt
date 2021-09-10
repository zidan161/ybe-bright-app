package com.example.ybebrightapp.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ybebrightapp.checkout.CheckoutActivity
import com.example.ybebrightapp.R
import com.example.ybebrightapp.databinding.ActivityProductBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Buy
import com.example.ybebrightapp.viewmodel.ViewModelFactory

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    private val list = arrayListOf<Buy>()

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Agent>("data")

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        viewModel.item.observe(this) {
            binding.tvItemCount.visibility = View.VISIBLE
            binding.tvItemCount.text = it.toString()
        }

        val fragment = ProductFragment()
        if (data != null) {
            val bundle = Bundle()
            bundle.putParcelable(MainActivity.DATA_KEY, data)
            fragment.arguments = bundle
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.payment_frame, fragment)
            .addToBackStack(null)
            .commit()

        binding.imageButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putParcelableArrayListExtra("data", list)
            startActivity(intent)
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.payment_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun addCheckoutItem(item: Buy) {
        list.add(item)
        viewModel.addItem()
    }
}