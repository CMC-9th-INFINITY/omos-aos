package com.infinity.omos.repository.record

import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.MyRecord

interface RecordRepository {

    suspend fun getMyRecords(userId: Int) : Result<List<MyRecord>>

    suspend fun getAllRecords(userId: Int) : Result<AllRecords>
}