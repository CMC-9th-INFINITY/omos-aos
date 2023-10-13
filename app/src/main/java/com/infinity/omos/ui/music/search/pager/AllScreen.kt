package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.data.music.artist.ArtistModel
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import kotlin.math.min

private const val MUSIC_MAX_SIZE = 4
private const val ALBUM_MAX_SIZE = 3
private const val ARTIST_MAX_SIZE = 3

@Composable
fun AllScreen(
    modifier: Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel(),
    onMusicMoreClick: () -> Unit,
    onAlbumMoreClick: () -> Unit,
    onArtistMoreClick: () -> Unit,
    onMusicClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit,
    onArtistClick: (String) -> Unit
) {
    val state = viewModel.searchState.collectAsState().value
    LaunchedEffect(state) {
        Timber.d("Update paging items")
    }

    AllScreen(
        modifier = modifier,
        musicStream = viewModel.musicStream,
        albumStream = viewModel.albumStream,
        artistStream = viewModel.artistStream,
        onMusicMoreClick = onMusicMoreClick,
        onAlbumMoreClick = onAlbumMoreClick,
        onArtistMoreClick = onArtistMoreClick,
        onMusicClick = onMusicClick,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick
    )
}

@Composable
fun AllScreen(
    modifier: Modifier = Modifier,
    musicStream: Flow<PagingData<MusicModel>>,
    albumStream: Flow<PagingData<AlbumModel>>,
    artistStream: Flow<PagingData<ArtistModel>>,
    onMusicMoreClick: () -> Unit = {},
    onAlbumMoreClick: () -> Unit = {},
    onArtistMoreClick: () -> Unit = {},
    onMusicClick: (String) -> Unit = {},
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
    val musicPagingItems: LazyPagingItems<MusicModel> = musicStream.collectAsLazyPagingItems()
    val albumPagingItems: LazyPagingItems<AlbumModel> = albumStream.collectAsLazyPagingItems()
    val artistPagingItems: LazyPagingItems<ArtistModel> = artistStream.collectAsLazyPagingItems()

    LazyColumn {
        item {
            PageHeader(
                title = stringResource(id = R.string.music),
                isVisibleMore = true,
                onMoreClick = onMusicMoreClick
            )
        }
        items(
            count = min(MUSIC_MAX_SIZE, musicPagingItems.itemCount),
            key = { index ->
                val music = musicPagingItems[index]
                "${music?.musicId ?: ""}${index}"
            }
        ) { index ->
            val music = musicPagingItems[index] ?: return@items
            MusicListItem(
                music = music,
                modifier = modifier
            ) {
                onMusicClick(music.musicId)
            }
        }

        item {
            PageHeader(
                title = stringResource(id = R.string.album),
                isVisibleMore = true,
                onMoreClick = onAlbumMoreClick
            )
        }
        items(
            count = min(ALBUM_MAX_SIZE, albumPagingItems.itemCount),
            key = { index ->
                val album = albumPagingItems[index]
                "${album?.albumId ?: ""}${index}"
            }
        ) { index ->
            val album = albumPagingItems[index] ?: return@items
            AlbumListItem(
                album = album,
                modifier = modifier
            ) {
                onAlbumClick(album.albumId)
            }
        }

        item {
            PageHeader(
                title = stringResource(id = R.string.artist),
                isVisibleMore = true,
                onMoreClick = onArtistMoreClick
            )
        }
        items(
            count = min(ARTIST_MAX_SIZE, artistPagingItems.itemCount),
            key = { index ->
                val artist = artistPagingItems[index]
                "${artist?.artistId ?: ""}${index}"
            }
        ) { index ->
            val artist = artistPagingItems[index] ?: return@items
            ArtistListItem(
                artist = artist,
                modifier = modifier
            ) {
                onArtistClick(artist.artistId)
            }
        }
    }
}

@Preview
@Composable
fun AllScreenPreview() {
    OmosTheme {
        AllScreen(
            musicStream = emptyFlow(),
            albumStream = emptyFlow(),
            artistStream = emptyFlow()
        )
    }
}