package com.example.ybebrightapp.mainfragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ybebrightapp.HowToActivity
import com.example.ybebrightapp.IngredientActivity
import com.example.ybebrightapp.Product
import com.example.ybebrightapp.R
import com.example.ybebrightapp.agent.AgentActivity
import com.example.ybebrightapp.databinding.FragmentHomeBinding
import com.example.ybebrightapp.product.*
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val images = listOf(R.drawable.download, R.drawable.download1, R.drawable.images)

        val imageListener = ImageListener { position, imageView ->
            imageView.setImageResource(images[position])
        }

        fragmentHomeBinding.mainCv.pageCount = 3
        fragmentHomeBinding.mainCv.setImageListener(imageListener)

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            with(fragmentHomeBinding){
                btnAgent.setOnClickListener {
                    val intent = Intent(context, AgentActivity::class.java)
                    startActivity(intent)
                }
                btnPrice.setOnClickListener {
                    val intent = Intent(context, ProductActivity::class.java)
                    startActivity(intent)
                }
                btnHowto.setOnClickListener {
                    val intent = Intent(context, HowToActivity::class.java)
                    startActivity(intent)
                }
                btnIngredient.setOnClickListener {
                    val intent = Intent(context, IngredientActivity::class.java)
                    startActivity(intent)
                }

                val data = mutableListOf<Product>()

                val name = resources.getStringArray(R.array.product_name)
                val image = resources.obtainTypedArray(R.array.product_image)

                for (i in name.indices) {
                    val product = Product(name[i], image.getResourceId(i, 0))
                    data.add(product)
                }
                image.recycle()

                val adapter = ProductAdapter(requireContext(), data)

                fragmentHomeBinding.rvProduct.apply {
                    this.adapter = adapter
                    layoutManager = GridLayoutManager(context, 2)
                }
                fragmentHomeBinding.rvProduct.setOnClickListener {
                    val intent = Intent(context, ProductActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}