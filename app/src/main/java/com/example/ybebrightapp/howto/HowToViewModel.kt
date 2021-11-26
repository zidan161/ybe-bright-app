package com.example.ybebrightapp.howto

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository

class HowToViewModel(private val repository: MainRepository) : ViewModel() {

    fun getHowTo(): List<HowTo> = repository.getHowTo()
}