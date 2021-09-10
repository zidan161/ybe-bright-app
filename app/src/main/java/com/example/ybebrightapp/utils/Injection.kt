package com.example.ybebrightapp.utils

import android.content.Context
import com.example.ybebrightapp.data.JsonHelper
import com.example.ybebrightapp.data.MainRepository
import com.example.ybebrightapp.data.local.LocalDataSource
import com.example.ybebrightapp.data.remote.RemoteDataSource

object Injection {

    fun provideRepository(context: Context): MainRepository {

        val remoteDataSource = RemoteDataSource()
        val localDataSource = LocalDataSource.getInstance(JsonHelper(context))

        return MainRepository.getInstance(remoteDataSource, localDataSource)
    }
}