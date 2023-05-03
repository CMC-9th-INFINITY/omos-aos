package com.infinity.omos.source.remote.today

import com.infinity.omos.api.TodayService
import com.infinity.omos.data.LovedMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.record.SumRecord
import javax.inject.Inject

class TodayRemoteDataSourceImpl @Inject constructor(
    private val todayService: TodayService
) : TodayRemoteDataSource {

    override suspend fun getFamousRecord(): Result<List<SumRecord>> {
        return Result.runCatching {
            todayService.getFamousRecord()
        }
    }

    override suspend fun getLovedMusic(userId: Int): Result<LovedMusic> {
        return Result.runCatching {
            todayService.getLovedMusic(userId)
        }
    }

    override suspend fun getTodayMusic(): Result<Music> {
        return Result.runCatching {
            todayService.getTodayMusic()
        }
    }

    override suspend fun getRecommendedDj(): Result<List<Profile>> {
        return Result.runCatching {
            todayService.getRecommendedDj()
        }
    }
}