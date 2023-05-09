package com.infinity.omos.api

import com.infinity.omos.data.*
import com.infinity.omos.data.music.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("api/search/album")
    fun getAlbum(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Album>>

    @GET("api/search/album/{albumId}")
    fun getAlbumDetail(
        @Path("albumId") albumId: String
    ): Call<List<Music>>

    @GET("api/search/artist")
    fun getArtist(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Artists>>

    @GET("api/search/artist/{artistId}/albums")
    fun getArtistAlbum(
        @Path("artistId") artistId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<Album>>

    @GET("api/search/artist/{artistId}/hot-tracks")
    fun getArtistMusic(
        @Path("artistId") artistId: String
    ): Call<List<ArtistMusic>>

    @GET("api/search/track")
    fun getMusic(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("type") type: Int = 0
    ): Call<List<Music>>

    @GET("api/search/track")
    fun getSearchMusic(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("type") type: Int = 1
    ): Call<List<SearchMusic>>
}