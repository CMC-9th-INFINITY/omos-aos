package com.infinity.omos.source.remote.music

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRemoteDataSourceImpl @Inject constructor(
    private val musicService: MusicService
) : MusicRemoteDataSource {

    override suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>> {
        return Result.runCatching {
            musicService.getMusicTitle(keyword)
        }
    }

    override fun getMusicStream(keyword: String): Flow<PagingData<Music>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { MusicPagingSource(musicService, keyword) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}