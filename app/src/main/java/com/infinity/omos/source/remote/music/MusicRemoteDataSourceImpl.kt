package com.infinity.omos.source.remote.music

import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.MusicTitle
import javax.inject.Inject

class MusicRemoteDataSourceImpl @Inject constructor(
    private val musicService: MusicService
) : MusicRemoteDataSource {

    override suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>> {
        return Result.runCatching {
            musicService.getMusicTitle(keyword)
        }
    }
}