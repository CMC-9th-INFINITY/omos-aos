package com.infinity.omos.di

import com.infinity.omos.api.AuthService
import com.infinity.omos.api.BlockService
import com.infinity.omos.api.FakeUserService
import com.infinity.omos.api.FollowService
import com.infinity.omos.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
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