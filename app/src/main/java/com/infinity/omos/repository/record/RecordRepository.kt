package com.infinity.omos.repository.record

import androidx.paging.PagingData
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyPageRecord
import com.infinity.omos.data.record.MyRecord
import com.infinity.omos.data.record.SaveRecordRequest
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun getMyRecords(userId: Int): Result<List<MyRecord>>

    suspend fun getAllRecords(userId: Int): Result<AllRecords>

    suspend fun getDetailRecord(postId: Int, userId: Int): Result<DetailRecord>

    fun getDetailRecordsStream(toUserId: Int): Flow<PagingData<DetailRecord>>

    suspend fun getMyPageRecords(userId: Int): Result<MyPageRecord>

    suspend fun saveRecord(record: SaveRecordRequest): NetworkResult

    suspend fun saveLikeRecord(postId: Int, userId: Int): NetworkResult

    suspend fun deleteLikeRecord(postId: Int, userId: Int): NetworkResult

    suspend fun saveScrapRecord(postId: Int, userId: Int): NetworkResult

    suspend fun deleteScrapRecord(postId: Int, userId: Int): NetworkResult
}