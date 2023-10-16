package com.infinity.omos.repository.music

import androidx.paging.PagingData
import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import com.infinity.omos.data.music.album.AlbumMusic
import com.infinity.omos.source.remote.music.MusicRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicRemoteDataSource: MusicRemoteDataSource
) : MusicRepository {

    override suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>> {
        return musicRemoteDataSource.getMusicTitle(keyword)
    }

    override fun getMusicStream(keyword: String): Flow<PagingData<Music>> {
        return musicRemoteDataSource.getMusicStream(keyword)
    }

    override fun getAlbumStream(keyword: String): Flow<PagingData<Album>> {
        return musicRemoteDataSource.getAlbumStream(keyword)
    }

    override fun getArtistStream(keyword: String): Flow<PagingData<Artist>> {
        return musicRemoteDataSource.getArtistStream(keyword)
    }

    override suspend fun getAlbumMusicList(albumId: String): Result<List<AlbumMusic>> {
        return musicRemoteDataSource.getAlbumMusicList(albumId)
    }

    override fun getArtistAlbumStream(artistId: String): Flow<PagingData<Album>> {
        return musicRemoteDataSource.getArtistAlbumStream(artistId)
    }
}