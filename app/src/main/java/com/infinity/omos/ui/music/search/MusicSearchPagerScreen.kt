package com.infinity.omos.ui.music.search

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.infinity.omos.R
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
    pages: Array<MusicSearchPage> = MusicSearchPage.values()
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

                }

                MusicSearchPage.MUSIC -> {

                }

                MusicSearchPage.ALBUM -> {

                }

                MusicSearchPage.ARTIST -> {

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