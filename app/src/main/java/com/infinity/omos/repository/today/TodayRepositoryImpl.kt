package com.infinity.omos.repository.today

import com.infinity.omos.data.music.LovedMusic
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.user.Profile
import com.infinity.omos.data.record.PreviewRecord
import com.infinity.omos.source.remote.today.TodayRemoteDataSource
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val todayRemoteDataSource: TodayRemoteDataSource
) : TodayRepository {

    override suspend fun getTodayMusic(): Result<Music> {
        return todayRemoteDataSource.getTodayMusic()
    }

    override suspend fun getFamousRecords(): Result<List<PreviewRecord>> {
        return todayRemoteDataSource.getFamousRecords()
    }

    override suspend fun getRecommendedDjs(): Result<List<Profile>> {
        return todayRemoteDataSource.getRecommendedDjs()
    }

    override suspend fun getLovedMusic(userId: Int): Result<LovedMusic> {
        return todayRemoteDataSource.getLovedMusic(userId)
    }
}