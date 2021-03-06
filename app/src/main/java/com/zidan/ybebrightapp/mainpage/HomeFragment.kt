package com.zidan.ybebrightapp.mainpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.zidan.ybebrightapp.*
import com.zidan.ybebrightapp.databinding.FragmentHomeBinding
import com.zidan.ybebrightapp.main.MainActivity
import com.zidan.ybebrightapp.model.Agent
import com.zidan.ybebrightapp.model.Product
import com.zidan.ybebrightapp.product.*
import com.synnapps.carouselview.ImageListener
import com.zidan.ybebrightapp.agent.AgentActivity
import com.zidan.ybebrightapp.howto.HowToActivity
import com.zidan.ybebrightapp.ingredients.IngredientActivity
import com.zidan.ybebrightapp.utils.showLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val slide = listOf(
            R.drawable.slide_dkter, R.drawable.slide_bali,
            R.drawable.slide_you, R.drawable.slide_free,
            R.drawable.slide_hadir, R.drawable.slide_terlaris
        )

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

                CoroutineScope(Dispatchers.IO).launch {
                    val name = resources.getStringArray(R.array.product_name)
                    val image = resources.obtainTypedArray(R.array.product_image)
                    val weight = resources.getIntArray(R.array.product_weight)

                    for (i in 0 until 2) {
                        val product = Product(name[i], image.getResourceId(i, 0), weight[i], true)
                        data.add(product)
                    }
                    data.add(Product("Paket Premium Glow", R.drawable.foto_pakecoming, weight[0], true))
                    data.add(Product("Fresh Cleanshing", R.drawable.foto_refresh, weight[0], false))
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