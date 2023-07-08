package com.infinity.omos.source.remote.music

import com.infinity.omos.data.music.MusicTitle

interface MusicRemoteDataSource {

    suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>>
}