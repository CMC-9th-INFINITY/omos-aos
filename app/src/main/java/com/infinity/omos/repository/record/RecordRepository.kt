package com.infinity.omos.repository.record

import com.infinity.omos.data.record.MyRecord

interface RecordRepository {

    suspend fun getMyRecords(userId: Int) : Result<List<MyRecord>>
}