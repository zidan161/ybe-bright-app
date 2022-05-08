package com.zidan.ybebrightapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zidan.ybebrightapp.ProfileViewModel
import com.zidan.ybebrightapp.agent.AgentViewModel
import com.zidan.ybebrightapp.data.JsonHelper
import com.zidan.ybebrightapp.data.MainRepository
import com.zidan.ybebrightapp.hidok.HiDokViewModel
import com.zidan.ybebrightapp.howto.HowToViewModel
import com.zidan.ybebrightapp.ingredients.IngredientsViewModel
import com.zidan.ybebrightapp.product.ProductViewModel
import com.zidan.ybebrightapp.utils.Injection

class ViewModelFactory(private val repository: MainRepository, private val helper: JsonHelper): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context), JsonHelper(context)).apply {
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
                ProductViewModel(repository, helper) as T
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