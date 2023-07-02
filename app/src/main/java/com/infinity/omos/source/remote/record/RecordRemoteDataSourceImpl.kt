package com.infinity.omos.source.remote.record

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.infinity.omos.api.RecordService
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
import com.infinity.omos.utils.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRemoteDataSourceImpl @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val recordService: RecordService
) : RecordRemoteDataSource {

    private val userId = dataStoreManager.getUserId()

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

    override fun getDetailRecordsStream(toUserId: Int): Flow<PagingData<DetailRecord>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = DETAIL_RECORD_PAGE_SIZE,
                pageSize = DETAIL_RECORD_PAGE_SIZE
            ),
            pagingSourceFactory = { DetailRecordPagingSource(recordService, userId, toUserId) }
        ).flow
    }

    companion object {
        private const val DETAIL_RECORD_PAGE_SIZE = 10
    }
}