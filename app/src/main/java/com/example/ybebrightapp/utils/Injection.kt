package com.example.ybebrightapp.utils

import android.content.Context
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.data.local.LocalDataSource

object Injection {

    fun provideRepository(context: Context): MainRepository {

        val localDataSource = LocalDataSource.getInstance(JsonHelper(context))

        return MainRepository.getInstance(localDataSource)
    }
}