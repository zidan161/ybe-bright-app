package com.zidan.ybebrightapp.ingredients

import androidx.lifecycle.ViewModel
import com.zidan.ybebrightapp.data.MainRepository

class IngredientsViewModel(private val repository: MainRepository) : ViewModel() {

    fun getIngredient(): List<Ingredient> = repository.getIngredients()
}