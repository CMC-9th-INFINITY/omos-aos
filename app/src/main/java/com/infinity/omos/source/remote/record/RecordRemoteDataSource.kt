package com.infinity.omos.source.remote.record

import androidx.paging.PagingData
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
import kotlinx.coroutines.flow.Flow

interface RecordRemoteDataSource {

    suspend fun getMyRecords(userId: Int) : Result<List<MyRecord>>

    suspend fun getAllRecords(userId: Int) : Result<AllRecords>

    fun getDetailRecordsStream(toUserId: Int): Flow<PagingData<DetailRecord>>
}