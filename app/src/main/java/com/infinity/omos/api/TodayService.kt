package com.infinity.omos.api

import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.SumRecord
import retrofit2.Call
import retrofit2.http.GET

interface TodayService {
    @GET("/api/today/famous-records-of-today")
    fun getFamousRecord(): Call<List<SumRecord>>

    @GET("/api/today/music-loved")
    fun getMyLoveMusic(): Call<Music>

    @GET("/api/today/music-of-today")
    fun getTodayMusic(): Call<Music>

    @GET("/api/today/recommend-dj")
    fun getRecommendDj(): Call<List<Profile>>
}