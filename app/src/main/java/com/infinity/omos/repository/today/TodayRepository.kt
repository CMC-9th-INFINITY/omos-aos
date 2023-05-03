package com.infinity.omos.repository.today

import com.infinity.omos.data.LovedMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.record.SumRecord

interface TodayRepository {

    suspend fun getFamousRecord(): Result<List<SumRecord>>

    suspend fun getLovedMusic(userId: Int): Result<LovedMusic>

    suspend fun getTodayMusic(): Result<Music>

    suspend fun getRecommendedDj(): Result<List<Profile>>
}