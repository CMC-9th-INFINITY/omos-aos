package com.infinity.omos.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  수정 필요
 */
object RetrofitAPI {
    private var instance : Retrofit? = null

    fun getInstnace() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}