package com.zidan.adminfarmasi

import android.content.Context

object Injection {

    fun provideRepository(context: Context): MainRepository {

        val remoteDataSource = RemoteDatasource()

        return MainRepository.getInstance(remoteDataSource)
    }
}