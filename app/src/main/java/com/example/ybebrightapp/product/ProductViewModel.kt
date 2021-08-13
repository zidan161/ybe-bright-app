package com.example.ybebrightapp.product

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository

class ProductViewModel(private val repository: MainRepository): ViewModel() {

    fun getProduct() = repository.getProduct()
}