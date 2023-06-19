package com.infinity.omos.source.remote.record

import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.MyRecord

interface RecordRemoteDataSource {

    suspend fun getMyRecords(userId: Int) : Result<List<MyRecord>>

    suspend fun getAllRecords(userId: Int) : Result<AllRecords>
}