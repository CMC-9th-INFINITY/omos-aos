package com.infinity.omos.source.remote.music

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.Music

private const val MUSIC_STARTING_PAGE_INDEX = 1

class MusicPagingSource(
    private val service: MusicService,
    private val keyword: String
) : PagingSource<Int, Music>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Music> {
        val page = params.key ?: MUSIC_STARTING_PAGE_INDEX
        return try {
            val response = service.getMusic(keyword, page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == MUSIC_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Music>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}