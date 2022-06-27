package com.infinity.omos.di

import com.infinity.omos.api.BlockService
import com.infinity.omos.api.FollowService
import com.infinity.omos.api.RetrofitAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBlockedService(): BlockService {
        return RetrofitAPI.getInstnace().create(BlockService::class.java)
    }

    @Singleton
    @Provides
    fun provideFollowService(): FollowService {
        return RetrofitAPI.getInstnace().create(FollowService::class.java)
    }
}