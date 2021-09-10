package com.example.ybebrightapp

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.model.Poin

class ProfileViewModel(private val repository: MainRepository): ViewModel() {

    fun getPoin(poin: Int): List<Poin> = repository.getPoin(poin)
}