package com.infinity.omos.source.remote.record

import com.infinity.omos.data.record.MyRecord

interface RecordRemoteDataSource {

    suspend fun getMyRecords(userId: Int) : Result<List<MyRecord>>
}