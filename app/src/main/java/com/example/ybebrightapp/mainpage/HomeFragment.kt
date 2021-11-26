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
import com.example.ybebrightapp.howto.HowToActivity
import com.example.ybebrightapp.ingredients.IngredientActivity
import com.example.ybebrightapp.main.MainActivity
import com.example.ybebrightapp.model.Agent
import com.example.ybebrightapp.model.Product
import com.example.ybebrightapp.product.*
import com.example.ybebrightapp.utils.showLoading
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val slide = listOf(R.drawable.slide_dkter, R.drawable.slide_you, R.drawable.slide_free,
            R.drawable.slide_hadir, R.drawable.slide_terlaris)

        val imageListener = ImageListener { position, imageView ->
            imageView.setImageResource(slide[position])
        }

        fragmentHomeBinding.mainCv.pageCount = slide.size
        fragmentHomeBinding.mainCv.setImageListener(imageListener)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            val member = arguments?.getParcelable<Agent>(MainActivity.DATA_KEY)

            with(fragmentHomeBinding){
                btnAgent.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        val loading = showLoading()
                        loading.show()
                        val intent = Intent(context, AgentActivity::class.java)
                        startActivity(intent)
                        loading.hide()
                    }
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

                CoroutineScope(Dispatchers.Default).launch {
                    val name = resources.getStringArray(R.array.product_name)
                    val image = resources.obtainTypedArray(R.array.product_image)

                    for (i in 0 until 2) {
                        val product = Product(name[i], image.getResourceId(i, 0), true)
                        data.add(product)
                    }
                    data.add(Product("Paket Premium Glow", R.drawable.foto_pakecoming, true))
                    data.add(Product("Fresh Cleanshing", R.drawable.foto_refresh, true))
                    image.recycle()
                }

                val status = member?.keagenan ?: "customer"
                val adapter = ProductAdapter(data, status) { _, _ ->
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