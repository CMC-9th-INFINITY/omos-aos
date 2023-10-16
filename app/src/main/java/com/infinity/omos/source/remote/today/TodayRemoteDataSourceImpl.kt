package com.infinity.omos.source.remote.today

import com.infinity.omos.api.TodayService
import com.infinity.omos.data.music.LovedMusic
import com.infinity.omos.data.music.Music
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.data.record.PreviewRecord
import javax.inject.Inject

class TodayRemoteDataSourceImpl @Inject constructor(
    private val todayService: TodayService
) : TodayRemoteDataSource {

    override suspend fun getFamousRecords(): Result<List<PreviewRecord>> {
        return Result.runCatching {
            todayService.getFamousRecords()
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

    override suspend fun getRecommendedDjs(): Result<List<Profile>> {
        return Result.runCatching {
            todayService.getRecommendedDjs()
        }
    }
}