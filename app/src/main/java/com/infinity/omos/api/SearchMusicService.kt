package com.infinity.omos.api

import com.infinity.omos.data.Album
import com.infinity.omos.data.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMusicService {
    @GET("api/search/track")
    fun getMusic(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Music>>
}