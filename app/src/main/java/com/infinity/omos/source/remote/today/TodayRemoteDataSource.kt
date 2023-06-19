package com.infinity.omos.source.remote.today

import com.infinity.omos.data.music.LovedMusic
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.user.Profile
import com.infinity.omos.data.record.PreviewRecord

interface TodayRemoteDataSource {

    suspend fun getTodayMusic(): Result<Music>

    suspend fun getFamousRecords(): Result<List<PreviewRecord>>

    suspend fun getRecommendedDjs(): Result<List<Profile>>

    suspend fun getLovedMusic(userId: Int): Result<LovedMusic>
}