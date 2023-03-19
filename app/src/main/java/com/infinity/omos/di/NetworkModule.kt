package com.infinity.omos.di

import com.infinity.omos.BuildConfig
import com.infinity.omos.api.AuthService
import com.infinity.omos.utils.DataStoreManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOmosInterceptor(
        dataStoreManager: DataStoreManager
    ): Interceptor {
        return Interceptor { chain ->
            val accessToken = runBlocking { dataStoreManager.tokenFlow.first().accessToken }
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        dataStoreManager: DataStoreManager,
        authService: Provider<AuthService> // Provider: 순환 참조 문제 해결
    ): Authenticator {
        val authenticator = Authenticator { _, response ->
            if (response.code == 401) {
                val oldToken = runBlocking { dataStoreManager.tokenFlow.first() }
                val newToken = authService.get().reissueToken(oldToken)
                runBlocking { dataStoreManager.saveToken(newToken) }

                response.request.newBuilder()
                    .addHeader("Authorization", "Bearer ${newToken.accessToken}")
                    .build()
            } else {
                null
            }
        }
        return authenticator
    }

    @Provides
    @Singleton
    fun provideOmosOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        omosInterceptor: Interceptor,
        tokenAuthenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(omosInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        val json = Json { ignoreUnknownKeys = true }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideOmosRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OMOS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}