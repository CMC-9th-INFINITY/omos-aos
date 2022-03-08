package com.infinity.omos.api

import com.infinity.omos.data.ArtistMusic
import com.infinity.omos.data.Artists
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistMusicService {
    @GET("api/search/artist/{artistId}/hot-tracks")
    fun getArtistMusic(
        @Path("artistId") artistId: String
    ): Call<List<ArtistMusic>>
}