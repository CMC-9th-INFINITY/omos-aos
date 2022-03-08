package com.infinity.omos.api

import com.infinity.omos.data.Album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistAlbumService {
    @GET("api/search/artist/{artistId}/albums")
    fun getArtistAlbum(
        @Path("artistId") artistId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Album>>
}