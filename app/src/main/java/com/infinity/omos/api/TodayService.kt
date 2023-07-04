package com.infinity.omos.api

import com.infinity.omos.data.music.LovedMusic
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.data.record.PreviewRecord
import retrofit2.http.GET
import retrofit2.http.Path

interface TodayService {

    @GET("/api/today/music-of-today")
    suspend fun getTodayMusic(): Music

    @GET("/api/today/famous-records-of-today")
    suspend fun getFamousRecords(): List<PreviewRecord>

    @GET("/api/today/recommend-dj")
    suspend fun getRecommendedDjs(): List<Profile>

    @GET("/api/today/music-loved/{userId}")
    suspend fun getLovedMusic(
        @Path("userId") userId: Int
    ): LovedMusic
}