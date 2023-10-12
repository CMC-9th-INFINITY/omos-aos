package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.infinity.omos.R
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.white
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun MusicListScreen(
    onMusicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel()
) {
    MusicListScreen(
        modifier = modifier,
        musicStream = viewModel.musicStream,
        onMusicClick = onMusicClick
    )
}

@Composable
fun MusicListScreen(
    musicStream: Flow<PagingData<MusicModel>>,
    modifier: Modifier = Modifier,
    onMusicClick: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(top = 29.dp)
                .padding(bottom = Dimens.PaddingNormal)
                .padding(horizontal = Dimens.PaddingNormal),
            text = stringResource(id = R.string.music),
            style = MaterialTheme.typography.titleMedium
        )

        val pagingItems: LazyPagingItems<MusicModel> = musicStream.collectAsLazyPagingItems()
        LazyColumn(modifier = modifier) {
            items(
                count = pagingItems.itemCount,
                key = {index ->
                    val music = pagingItems[index]
                    "${music?.musicId ?: ""}${index}"
                }
            ) { index ->
                val music = pagingItems[index] ?: return@items
                MusicListItem(
                    music = music,
                    modifier = modifier
                ) {
                    onMusicClick(music.musicId)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MusicListItem(
    music: MusicModel,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = Dimens.PaddingNormal)
            .padding(bottom = Dimens.PaddingNormal),
        onClick = onClick,
        color = MaterialTheme.colorScheme.background
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .size(52.dp),
                model = music.albumImageUrl,
                contentDescription = stringResource(id = R.string.album_cover)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp)
                    .weight(1f),
            ) {
                Text(
                    text = music.musicTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = music.artistsAndAlbumTitle,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.write),
                colorFilter = ColorFilter.tint(white)
            )
        }
    }
}

@Preview
@Composable
fun MusicListScreenPreview() {
    OmosTheme {
        MusicListScreen(musicStream = emptyFlow())
    }
}