package com.infinity.omos.source.remote.music

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.album.Album

private const val ALBUM_STARTING_PAGE_INDEX = 0

class AlbumPagingSource(
    private val service: MusicService,
    private val keyword: String
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val page = params.key ?: ALBUM_STARTING_PAGE_INDEX
        return try {
            val response = service.getAlbumList(keyword, params.loadSize, page * params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == ALBUM_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}