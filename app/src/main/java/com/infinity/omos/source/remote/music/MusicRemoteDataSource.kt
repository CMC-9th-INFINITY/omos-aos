package com.infinity.omos.source.remote.music

import androidx.paging.PagingData
import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import kotlinx.coroutines.flow.Flow

interface MusicRemoteDataSource {

    suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>>

    fun getMusicStream(keyword: String): Flow<PagingData<Music>>

    fun getAlbumStream(keyword: String): Flow<PagingData<Album>>

    fun getArtistStream(keyword: String): Flow<PagingData<Artist>>
}