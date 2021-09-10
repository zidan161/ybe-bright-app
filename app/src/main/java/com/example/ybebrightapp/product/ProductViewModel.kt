package com.example.ybebrightapp.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository

class ProductViewModel(private val repository: MainRepository): ViewModel() {

    var item = MutableLiveData<Int>()
    var itemCount = 0

    fun addItem() {
        itemCount++
        item.value = itemCount
    }

    fun getProduct() = repository.getProduct()
}