package com.infinity.omos.api

import com.infinity.omos.data.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumDetailService {
    @GET("api/search/album/{albumId}")
    fun getAlbumDetail(
        @Path("albumId") albumId: String
    ): Call<List<Music>>
}