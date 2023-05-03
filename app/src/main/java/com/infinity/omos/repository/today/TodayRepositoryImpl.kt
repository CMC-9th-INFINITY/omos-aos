package com.infinity.omos.repository.today

import com.infinity.omos.data.LovedMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.record.SumRecord
import com.infinity.omos.source.remote.today.TodayRemoteDataSource
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val todayRemoteDataSource: TodayRemoteDataSource
) : TodayRepository {

    override suspend fun getFamousRecord(): Result<List<SumRecord>> {
        return todayRemoteDataSource.getFamousRecord()
    }

    override suspend fun getLovedMusic(userId: Int): Result<LovedMusic> {
        return todayRemoteDataSource.getLovedMusic(userId)
    }

    override suspend fun getTodayMusic(): Result<Music> {
        return todayRemoteDataSource.getTodayMusic()
    }

    override suspend fun getRecommendedDj(): Result<List<Profile>> {
        return todayRemoteDataSource.getRecommendedDj()
    }
}