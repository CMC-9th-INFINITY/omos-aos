package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.infinity.omos.R
import com.infinity.omos.data.music.artist.ArtistModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.grey_04
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ArtistListScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel(),
    onArtistClick: (ArtistModel) -> Unit,
    onMoreClick: () -> Unit = {}
) {
    ArtistListScreen(
        modifier = modifier,
        artistStream = viewModel.artistStream,
        onArtistClick = onArtistClick,
        onMoreClick = onMoreClick
    )
}

@Composable
fun ArtistListScreen(
    modifier: Modifier = Modifier,
    artistStream: Flow<PagingData<ArtistModel>>,
    onArtistClick: (ArtistModel) -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val pagingItems: LazyPagingItems<ArtistModel> = artistStream.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier) {
        item {
            PageHeader(
                title = stringResource(id = R.string.artist),
                isVisibleMore = false,
                onMoreClick = onMoreClick
            )
        }
        items(
            count = pagingItems.itemCount,
            key = { index ->
                val artist = pagingItems[index]
                "${artist?.artistId ?: ""}${index}"
            }
        ) { index ->
            val artist = pagingItems[index] ?: return@items
            ArtistListItem(
                artist = artist,
                modifier = modifier
            ) {
                onArtistClick(artist)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListItem(
    artist: ArtistModel,
    modifier: Modifier,
    onArtistClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = Dimens.PaddingNormal)
            .padding(bottom = Dimens.PaddingNormal),
        onClick = onArtistClick,
        color = MaterialTheme.colorScheme.background
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(color = grey_04),
                imageModel = { artist.artistImageUrl },
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
                    text = artist.artistName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = artist.genres,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun ArtistListScreenPreview() {
    OmosTheme {
        ArtistListScreen(artistStream = emptyFlow())
    }
}