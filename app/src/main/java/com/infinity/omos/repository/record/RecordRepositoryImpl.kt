package com.infinity.omos.repository.record

import androidx.paging.PagingData
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyPageRecord
import com.infinity.omos.data.record.MyRecord
import com.infinity.omos.data.record.SaveRecordRequest
import com.infinity.omos.source.remote.record.RecordRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordRemoteDataSource: RecordRemoteDataSource
) : RecordRepository {

    override suspend fun getMyRecords(userId: Int): Result<List<MyRecord>> {
        return recordRemoteDataSource.getMyRecords(userId)
    }

    override suspend fun getAllRecords(userId: Int): Result<AllRecords> {
        return recordRemoteDataSource.getAllRecords(userId)
    }

    override suspend fun getDetailRecord(postId: Int, userId: Int): Result<DetailRecord> {
        return recordRemoteDataSource.getDetailRecord(postId, userId)
    }

    override fun getDetailRecordsStream(toUserId: Int): Flow<PagingData<DetailRecord>> {
        return recordRemoteDataSource.getDetailRecordsStream(toUserId)
    }

    override suspend fun getMyPageRecords(userId: Int): Result<MyPageRecord> {
        return recordRemoteDataSource.getMyPageRecords(userId)
    }

    override suspend fun saveRecord(record: SaveRecordRequest): NetworkResult {
        return recordRemoteDataSource.saveRecord(record)
    }
}