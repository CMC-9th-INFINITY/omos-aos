package com.infinity.omos.source.remote.record

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.infinity.omos.api.RecordService
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRemoteDataSourceImpl @Inject constructor(
    private val recordService: RecordService
) : RecordRemoteDataSource {

    override suspend fun getMyRecords(userId: Int): Result<List<MyRecord>> {
        return Result.runCatching {
            recordService.getMyRecords(userId)
        }
    }

    override suspend fun getAllRecords(userId: Int): Result<AllRecords> {
        return Result.runCatching {
            recordService.getAllRecords(userId)
        }
    }

    override fun getDetailRecordsStream(userId: Int): Flow<PagingData<DetailRecord>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = DETAIL_RECORD_PAGE_SIZE,
                pageSize = DETAIL_RECORD_PAGE_SIZE
            ),
            pagingSourceFactory = { DetailRecordPagingSource(recordService, userId) }
        ).flow
    }

    companion object {
        private const val DETAIL_RECORD_PAGE_SIZE = 10
    }
}