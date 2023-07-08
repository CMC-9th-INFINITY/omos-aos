package com.infinity.omos.repository.music

import com.infinity.omos.data.music.MusicTitle
import com.infinity.omos.source.remote.music.MusicRemoteDataSource
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicRemoteDataSource: MusicRemoteDataSource
) : MusicRepository {

    override suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>> {
        return musicRemoteDataSource.getMusicTitle(keyword)
    }
}