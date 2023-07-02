package com.infinity.omos.repository.record

import androidx.paging.PagingData
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
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

    override fun getDetailRecordsStream(toUserId: Int): Flow<PagingData<DetailRecord>> {
        return recordRemoteDataSource.getDetailRecordsStream(toUserId)
    }
}