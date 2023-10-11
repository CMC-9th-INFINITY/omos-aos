package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun AllScreen(
    viewModel: MusicSearchViewModel = hiltViewModel(),
    modifier: Modifier,
) {
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
    val pagingItems: LazyPagingItems<MusicModel> = musicStream.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = pagingItems.itemCount,
            key = {index ->
                val music = pagingItems[index]
                "${music?.musicId ?: ""}${index}"
            }
        ) { index ->
            val music = pagingItems[index] ?: return@items
            Text(text = music.musicTitle)
        }
    }
}