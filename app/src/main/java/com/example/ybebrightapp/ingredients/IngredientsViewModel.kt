package com.example.ybebrightapp.ingredients

import androidx.lifecycle.ViewModel
import com.example.ybebrightapp.data.MainRepository

class IngredientsViewModel(private val repository: MainRepository) : ViewModel() {

    fun getIngredient(): List<Ingredient> = repository.getIngredients()
}