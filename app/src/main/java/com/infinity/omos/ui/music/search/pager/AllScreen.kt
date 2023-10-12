package com.infinity.omos.ui.music.search.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

private const val MUSIC_MAX_SIZE = 4
private const val ALBUM_MAX_SIZE = 3
private const val ARTIST_MAX_SIZE = 3

@Composable
fun AllScreen(
    modifier: Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel(),
) {
    val state = viewModel.searchState.collectAsState().value
    LaunchedEffect(state) {
        Timber.d("Update paging items")
    }

    AllScreen(
        musicStream = viewModel.musicStream,
        modifier = modifier
    )
}

@Composable
fun AllScreen(
    musicStream: Flow<PagingData<MusicModel>>,
    modifier: Modifier = Modifier
) {
    MusicListScreen(
        musicStream = musicStream,
        modifier = modifier,
        itemCount = MUSIC_MAX_SIZE
    )
}

@Preview
@Composable
fun AllScreenPreview() {
    OmosTheme {
        AllScreen(musicStream = emptyFlow())
    }
}