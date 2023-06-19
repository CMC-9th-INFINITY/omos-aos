package com.infinity.omos.repository.record

import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.MyRecord
import com.infinity.omos.source.remote.record.RecordRemoteDataSource
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
}