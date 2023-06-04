package com.infinity.omos.source.remote.record

import com.infinity.omos.api.RecordService
import com.infinity.omos.data.record.MyRecord
import javax.inject.Inject

class RecordRemoteDataSourceImpl @Inject constructor(
    private val recordService: RecordService
) : RecordRemoteDataSource {

    override suspend fun getMyRecords(userId: Int): Result<List<MyRecord>> {
        return Result.runCatching {
            recordService.getMyRecords(userId)
        }
    }
}