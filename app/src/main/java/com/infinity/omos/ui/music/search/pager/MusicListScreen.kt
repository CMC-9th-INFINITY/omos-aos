package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.infinity.omos.R
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.grey_04
import com.infinity.omos.ui.theme.white
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

@Composable
fun MusicListScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel(),
    onMusicClick: (String) -> Unit,
    onMoreClick: () -> Unit = {},
) {
    val state = viewModel.searchState.collectAsState().value
    LaunchedEffect(state) {
        Timber.d("Update paging items")
    }

    MusicListScreen(
        modifier = modifier,
        musicStream = viewModel.musicStream,
        onMusicClick = onMusicClick,
        onMoreClick = onMoreClick
    )
}

@Composable
fun MusicListScreen(
    modifier: Modifier = Modifier,
    musicStream: Flow<PagingData<MusicModel>>,
    onMusicClick: (String) -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val pagingItems: LazyPagingItems<MusicModel> = musicStream.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier) {
        item {
            PageHeader(
                title = stringResource(id = R.string.music),
                isVisibleMore = false,
                onMoreClick = onMoreClick
            )
        }
        items(
            count = pagingItems.itemCount,
            key = { index ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListItem(
    music: MusicModel,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(start = Dimens.PaddingNormal)
            .padding(bottom = Dimens.PaddingNormal),
        onClick = onClick,
        color = MaterialTheme.colorScheme.background
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .size(52.dp)
                    .background(color = grey_04),
                imageModel = { music.albumImageUrl },
                failure = {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = stringResource(id = R.string.music_icon)
                    )
                }
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
                    .padding(horizontal = Dimens.PaddingNormal)
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
fun MusicListScreenPreview(
    @PreviewParameter(MusicListPreviewParamProvider::class) musicData: PagingData<MusicModel>
) {
    OmosTheme {
        val musicStream = remember { MutableStateFlow(musicData) }
        MusicListScreen(musicStream = musicStream.asStateFlow())
    }
}

private class MusicListPreviewParamProvider : PreviewParameterProvider<PagingData<MusicModel>> {
    override val values: Sequence<PagingData<MusicModel>> =
        sequenceOf(
            PagingData.empty(),
            PagingData.from(
                listOf(
                    MusicModel(
                        musicId = "0",
                        musicTitle = "음악 타이틀",
                        artists = listOf(
                            Artist(
                                artistId = "0",
                                artistImageUrl = "",
                                artistName = "아티스트 이름"
                            )
                        ),
                        albumTitle = "앨범 타이틀",
                        albumImageUrl = "",
                        artistsAndAlbumTitle = "가수 이름 - 앨범 타이틀"
                    )
                )
            )
        )
}