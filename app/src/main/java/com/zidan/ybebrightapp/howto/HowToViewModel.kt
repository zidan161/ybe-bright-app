package com.zidan.ybebrightapp.howto

import androidx.lifecycle.ViewModel
import com.zidan.ybebrightapp.data.MainRepository

class HowToViewModel(private val repository: MainRepository) : ViewModel() {

    fun getHowTo(): List<HowTo> = repository.getHowTo()
}