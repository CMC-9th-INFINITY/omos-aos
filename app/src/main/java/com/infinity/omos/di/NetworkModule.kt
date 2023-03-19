package com.infinity.omos.di

import com.infinity.omos.BuildConfig
import com.infinity.omos.api.AuthService
import com.infinity.omos.api.BlockService
import com.infinity.omos.api.FakeUserService
import com.infinity.omos.api.FollowService
import com.infinity.omos.api.UserService
import com.infinity.omos.utils.DataStoreManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    fun provideOmosOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        omosInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(omosInterceptor)
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    /*@Provides
    @Singleton
    fun provideReissueInterceptor(
        service: AuthService,
        dataStoreManager: DataStoreManager
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == 401) {
                val token = runBlocking { dataStoreManager.tokenFlow.first() }
                service.reissueToken(token)
            }



            val jsonString = response.body?.string() ?: ""
            val json = Json { ignoreUnknownKeys = true }
            val result = json.decodeFromString<WeatherContainerResponse>(jsonString)
            val weather = result.weathers.first()
            val type = getWeatherTypeById(weather.id)
            val main = result.main
            response.newBuilder()
                .message(response.message)
                .body(
                    Json.encodeToString(
                        WeatherResponse(
                            weather.id,
                            type,
                            weather.icon,
                            main.temperature
                        )
                    ).toResponseBody()
                )
                .build()
        }
    }*/

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ): UserService {
        return retrofit.create(UserService::class.java)
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