package com.example.ybebrightapp.mainpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ybebrightapp.*
import com.example.ybebrightapp.agent.AgentActivity
import com.example.ybebrightapp.databinding.FragmentHomeBinding
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.product.*
import com.example.ybebrightapp.utils.showLoading
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val images = listOf(R.drawable.slide, R.drawable.slide_1)

        val imageListener = ImageListener { position, imageView ->
            imageView.setImageResource(images[position])
        }

        fragmentHomeBinding.mainCv.pageCount = 2
        fragmentHomeBinding.mainCv.setImageListener(imageListener)

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            val member = arguments?.getParcelable<Agent>(MainActivity.DATA_KEY)

            with(fragmentHomeBinding){
                btnAgent.setOnClickListener {
                    val loading = showLoading()
                    loading.show()
                    val intent = Intent(context, AgentActivity::class.java)
                    startActivity(intent)
                    loading.hide()
                }
                btnPrice.setOnClickListener {
                    val intent = Intent(context, ProductActivity::class.java)
                    if (member != null){
                        intent.putExtra("data", member)
                    }
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
                    val product = Product(name[i], image.getResourceId(i, 0), true)
                    data.add(product)
                }
                image.recycle()

                val status = member?.status ?: "customer"
                val adapter = ProductAdapter(requireContext(), data, status) {
                    val intent = Intent(context, ProductActivity::class.java)
                    intent.putExtra("data", member)
                    startActivity(intent)
                }

                fragmentHomeBinding.rvProduct.apply {
                    this.adapter = adapter
                    layoutManager = GridLayoutManager(context, 2)
                }
            }
        }
    }
}