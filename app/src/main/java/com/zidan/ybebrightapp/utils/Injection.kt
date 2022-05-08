package com.zidan.ybebrightapp.utils

import android.content.Context
import com.zidan.ybebrightapp.data.JsonHelper
import com.zidan.ybebrightapp.data.MainRepository
import com.zidan.ybebrightapp.data.local.LocalDataSource
import com.zidan.ybebrightapp.data.remote.RemoteDataSource

object Injection {

    fun provideRepository(context: Context): MainRepository {

        val remoteDataSource = RemoteDataSource()
        val localDataSource = LocalDataSource.getInstance(JsonHelper(context))

        return MainRepository.getInstance(remoteDataSource, localDataSource)
    }
}