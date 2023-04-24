package com.infinity.omos.di

import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.repository.auth.AuthRepositoryImpl
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}