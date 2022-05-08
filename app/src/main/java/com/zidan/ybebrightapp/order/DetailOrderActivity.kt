package com.zidan.ybebrightapp.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidan.ybebrightapp.databinding.ActivityOrderDetailBinding

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Order>("data")!!

        with(binding) {
            tvName.text = data.name
            tvStatus.text = data.status
            tvValAddress.text = data.address
            tvValPhone.text = data.phone
            tvValCourier.text = "${data.courier} ${data.cost}"

            val adapter = ItemAdapter(data.buy!!)
            rvItem.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(this@DetailOrderActivity)
            }
        }
    }
}