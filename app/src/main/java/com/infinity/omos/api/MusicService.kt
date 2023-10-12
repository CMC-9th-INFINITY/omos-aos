package com.infinity.omos.api

import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    @GET("api/search/track")
    suspend fun getMusicTitleList(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("type") type: Int = 1
    ): List<MusicTitle>

    @GET("api/search/track")
    suspend fun getMusicList(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("type") type: Int = 0
    ): List<Music>

    @GET("api/search/album")
    suspend fun getAlbumList(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<Album>

    @GET("api/search/artist")
    suspend fun getArtistList(
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<Artist>
}