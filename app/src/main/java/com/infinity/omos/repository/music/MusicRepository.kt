package com.infinity.omos.repository.music

import androidx.paging.PagingData
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.music.MusicTitle
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    suspend fun getMusicTitle(keyword: String): Result<List<MusicTitle>>

    fun getMusicStream(keyword: String): Flow<PagingData<Music>>
}