package com.infinity.omos.di

import com.infinity.omos.repository.FakeAuthRepository
import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
interface FakeRepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(
        repository: FakeAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        repository: UserRepositoryImpl
    ): UserRepository
}