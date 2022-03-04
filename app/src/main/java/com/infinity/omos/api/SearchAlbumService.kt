package com.infinity.omos.api

import com.infinity.omos.data.Album
import com.infinity.omos.data.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAlbumService {
    @GET("api/search/album")
    fun getAlbum(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Album>>
}