package com.infinity.omos.repository.record

import androidx.paging.PagingData
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun getMyRecords(userId: Int): Result<List<MyRecord>>

    suspend fun getAllRecords(userId: Int): Result<AllRecords>

    fun getDetailRecordsStream(userId: Int): Flow<PagingData<DetailRecord>>
}