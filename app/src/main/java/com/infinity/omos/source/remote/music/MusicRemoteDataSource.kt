package com.infinity.omos.source.remote.music

import androidx.paging.PagingData
import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import com.infinity.omos.data.music.album.AlbumMusic
import kotlinx.coroutines.flow.Flow

interface MusicRemoteDataSource {

    suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>>

    suspend fun getMusic(musicId: String): Result<Music>

    fun getMusicStream(keyword: String): Flow<PagingData<Music>>

    fun getAlbumStream(keyword: String): Flow<PagingData<Album>>

    fun getArtistStream(keyword: String): Flow<PagingData<Artist>>

    suspend fun getAlbumMusicList(albumId: String): Result<List<AlbumMusic>>

    fun getArtistAlbumStream(artistId: String): Flow<PagingData<Album>>
}