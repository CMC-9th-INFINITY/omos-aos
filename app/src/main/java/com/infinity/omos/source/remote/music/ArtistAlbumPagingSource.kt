package com.infinity.omos.source.remote.music

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity.omos.api.MusicService
import com.infinity.omos.data.music.album.Album

private const val ARTIST_ALBUM_STARTING_PAGE_INDEX = 0

class ArtistAlbumPagingSource(
    private val service: MusicService,
    private val artistId: String
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val page = params.key ?: ARTIST_ALBUM_STARTING_PAGE_INDEX
        return try {
            val response = service.getArtistAlbumList(artistId, params.loadSize, page * params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == ARTIST_ALBUM_STARTING_PAGE_INDEX) null else page - 1,
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