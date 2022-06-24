package com.infinity.omos.di

import com.infinity.omos.api.BlockedService
import com.infinity.omos.api.RetrofitAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBlockedService(): BlockedService {
        return RetrofitAPI.getInstnace().create(BlockedService::class.java)
    }
}