package com.infinity.omos.api

import com.infinity.omos.BuildConfig
import com.infinity.omos.utils.GlobalApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 *  수정 필요
 */
object RetrofitAPI {
    private var movieInstance: Retrofit? = null
    var instance: Retrofit? = null

    fun getMovieInstnace(): Retrofit {
        if (movieInstance == null) {
            movieInstance = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return movieInstance!!
    }

    fun getInstnace(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(provideOkHttpClient(AppInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val token = "Bearer ${GlobalApplication.prefs.getString("accessToken")}"
            val newRequest = request().newBuilder()
                .addHeader("Authorization", token)
                .build()

            proceed(newRequest)
        }
    }
}