package com.infinity.omos.repository.music

import com.infinity.omos.data.music.MusicTitle

interface MusicRepository {

    suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>>
}