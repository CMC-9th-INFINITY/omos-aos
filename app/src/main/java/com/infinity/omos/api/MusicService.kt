package com.infinity.omos.api

import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    @GET("api/search/track")
    suspend fun getMusic(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("type") type: Int = 0
    ): List<Music>

    @GET("api/search/track")
    suspend fun getMusicTitle(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("type") type: Int = 1
    ): List<MusicTitle>
}