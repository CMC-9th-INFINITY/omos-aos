package com.infinity.omos.ui.music.search

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.ui.music.search.pager.AlbumListScreen
import com.infinity.omos.ui.music.search.pager.AllScreen
import com.infinity.omos.ui.music.search.pager.ArtistListScreen
import com.infinity.omos.ui.music.search.pager.MusicListScreen
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.grey_03
import kotlinx.coroutines.launch

enum class MusicSearchPage(
    @StringRes val titleResId: Int
) {
    ALL(R.string.all),
    MUSIC(R.string.music),
    ALBUM(R.string.album),
    ARTIST(R.string.artist)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicSearchPagerScreen(
    viewModel: MusicSearchViewModel = hiltViewModel(),
    pages: Array<MusicSearchPage> = MusicSearchPage.values(),
    onMusicClick: (String) -> Unit = {},
    onAlbumClick: (AlbumModel) -> Unit = {},
    onArtistClick: (String) -> Unit = {},
) {
    val pagerState = rememberPagerState()

    Column {
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title) },
                    unselectedContentColor = grey_03
                )
            }
        }

        // Pages
        HorizontalPager(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pageCount = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                MusicSearchPage.ALL -> {
                    AllScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onMusicMoreClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(MusicSearchPage.MUSIC.ordinal) }
                        },
                        onAlbumMoreClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(MusicSearchPage.ALBUM.ordinal) }
                        },
                        onArtistMoreClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(MusicSearchPage.ARTIST.ordinal) }
                        },
                        onMusicClick = onMusicClick,
                        onAlbumClick = onAlbumClick,
                        onArtistClick = onArtistClick
                    )
                }

                MusicSearchPage.MUSIC -> {
                    MusicListScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onMusicClick = onMusicClick,
                    )
                }

                MusicSearchPage.ALBUM -> {
                    AlbumListScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onAlbumClick = onAlbumClick
                    )
                }

                MusicSearchPage.ARTIST -> {
                    ArtistListScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onArtistClick = onArtistClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MusicSearchPagerScreenPreview() {
    OmosTheme {
        MusicSearchPagerScreen()
    }
}