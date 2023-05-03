package com.infinity.omos.api

import com.infinity.omos.data.LovedMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.record.SumRecord
import retrofit2.http.GET
import retrofit2.http.Path

interface TodayService {
    @GET("/api/today/famous-records-of-today")
    suspend fun getFamousRecord(): List<SumRecord>

    @GET("/api/today/music-loved/{userId}")
    suspend fun getLovedMusic(
        @Path("userId") userId: Int
    ): LovedMusic

    @GET("/api/today/music-of-today")
    suspend fun getTodayMusic(): Music

    @GET("/api/today/recommend-dj")
    suspend fun getRecommendedDj(): List<Profile>
}