package com.example.ybebrightapp.main

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.MainRepository

class MainViewModel(private val repository: MainRepository): ViewModel() {

    fun getNews() = repository.getNews()
}