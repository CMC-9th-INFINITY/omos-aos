package com.infinity.omos.source.remote.music

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import com.infinity.omos.data.music.album.AlbumMusic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRemoteDataSourceImpl @Inject constructor(
    private val musicService: MusicService
) : MusicRemoteDataSource {

    override suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>> {
        return Result.runCatching {
            musicService.getMusicTitleList(keyword)
        }
    }

    override suspend fun getMusic(musicId: String): Result<Music> {
        return Result.runCatching {
            musicService.getMusic(musicId)
        }
    }

    override fun getMusicStream(keyword: String): Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { MusicPagingSource(musicService, keyword) }
        ).flow
    }

    override fun getAlbumStream(keyword: String): Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { AlbumPagingSource(musicService, keyword) }
        ).flow
    }

    override fun getArtistStream(keyword: String): Flow<PagingData<Artist>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { ArtistPagingSource(musicService, keyword) }
        ).flow
    }

    override suspend fun getAlbumMusicList(albumId: String): Result<List<AlbumMusic>> {
        return Result.runCatching {
            musicService.getAlbumMusicList(albumId)
        }
    }

    override fun getArtistAlbumStream(artistId: String): Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { ArtistAlbumPagingSource(musicService, artistId) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 15
    }
}