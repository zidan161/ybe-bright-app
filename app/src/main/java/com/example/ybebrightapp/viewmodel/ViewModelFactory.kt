package com.example.ybebrightapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ybebrightapp.ProfileViewModel
import com.example.ybebrightapp.agent.AgentViewModel
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.hidok.HiDokViewModel
import com.example.ybebrightapp.howto.HowToViewModel
import com.example.ybebrightapp.ingredients.IngredientsViewModel
import com.example.ybebrightapp.product.ProductViewModel
import com.example.ybebrightapp.utils.Injection

class ViewModelFactory(private val repository: MainRepository): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                        instance = this
                    }
                }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AgentViewModel::class.java) -> {
                AgentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(IngredientsViewModel::class.java) -> {
                IngredientsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HowToViewModel::class.java) -> {
                HowToViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HiDokViewModel::class.java) -> {
                HiDokViewModel() as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}