package com.infinity.omos.api

import com.infinity.omos.data.Album
import com.infinity.omos.data.Artists
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchArtistService {
    @GET("api/search/artist")
    fun getArtist(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Artists>>
}