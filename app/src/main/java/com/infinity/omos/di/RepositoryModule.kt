package com.infinity.omos.di

import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.repository.auth.AuthRepositoryImpl
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.repository.record.RecordRepositoryImpl
import com.infinity.omos.repository.today.TodayRepository
import com.infinity.omos.repository.today.TodayRepositoryImpl
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

    @Binds
    @Singleton
    fun bindTodayRepository(
        todayRepositoryImpl: TodayRepositoryImpl
    ): TodayRepository

    @Binds
    @Singleton
    fun bindRecordRepository(
        recordRepositoryImpl: RecordRepositoryImpl
    ): RecordRepository
}