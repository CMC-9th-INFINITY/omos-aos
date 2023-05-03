package com.infinity.omos.di

import com.infinity.omos.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(
        retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideTodayService(
        retrofit: Retrofit
    ): TodayService {
        return retrofit.create(TodayService::class.java)
    }

    @Provides
    @Singleton
    fun provideBlockedService(
        retrofit: Retrofit
    ): BlockService {
        return retrofit.create(BlockService::class.java)
    }

    @Provides
    @Singleton
    fun provideFollowService(
        retrofit: Retrofit
    ): FollowService {
        return retrofit.create(FollowService::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeUserService(
        retrofit: Retrofit
    ): FakeUserService {
        return retrofit.create(FakeUserService::class.java)
    }
}