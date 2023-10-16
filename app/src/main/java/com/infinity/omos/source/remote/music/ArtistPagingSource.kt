package com.infinity.omos.source.remote.music

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.artist.Artist

private const val ARTIST_STARTING_PAGE_INDEX = 0

class ArtistPagingSource(
    private val service: MusicService,
    private val keyword: String
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        val page = params.key ?: ARTIST_STARTING_PAGE_INDEX
        return try {
            val response = service.getArtistList(keyword, params.loadSize, page * params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == ARTIST_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}