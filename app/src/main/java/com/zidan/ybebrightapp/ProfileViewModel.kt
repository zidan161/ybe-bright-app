package com.zidan.ybebrightapp

import androidx.lifecycle.ViewModel
import com.zidan.ybebrightapp.data.MainRepository
import com.zidan.ybebrightapp.model.Poin

class ProfileViewModel(private val repository: MainRepository): ViewModel() {

    fun getPoin(poin: Int): List<Poin> = repository.getPoin(poin)
}