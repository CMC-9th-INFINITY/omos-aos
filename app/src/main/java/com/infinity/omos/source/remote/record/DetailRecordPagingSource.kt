package com.infinity.omos.source.remote.record

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity.omos.api.RecordService
import com.infinity.omos.data.record.DetailRecord

class DetailRecordPagingSource(
    private val recordService: RecordService,
    private val userId: Int
) : PagingSource<Int, DetailRecord>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DetailRecord> {
        val page = params.key
        return try {
            val response = recordService.getDetailRecords(userId, page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.last().recordId
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DetailRecord>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}