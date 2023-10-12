package com.infinity.omos.ui.music.search.pager

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.infinity.omos.data.music.artist.ArtistModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.music.search.MusicSearchViewModel
import com.infinity.omos.ui.theme.OmosTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import kotlin.math.min

@Composable
fun ArtistListScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicSearchViewModel = hiltViewModel(),
    itemCount: Int = -1,
    onItemClick: (String) -> Unit,
    onMoreClick: () -> Unit = {}
) {
    val state = viewModel.searchState.collectAsState().value
    LaunchedEffect(state) {
        Timber.d("Update paging items")
    }

    ArtistListScreen(
        modifier = modifier,
        artistStream = viewModel.artistStream,
        itemCount = itemCount,
        onItemClick = onItemClick,
        onMoreClick = onMoreClick
    )
}

@Composable
fun ArtistListScreen(
    modifier: Modifier = Modifier,
    artistStream: Flow<PagingData<ArtistModel>>,
    itemCount: Int = -1,
    onItemClick: (String) -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val pagingItems: LazyPagingItems<ArtistModel> = artistStream.collectAsLazyPagingItems()
    val (count, isVisibleMore) = if (itemCount == -1) {
        pagingItems.itemCount to false
    } else {
        itemCount to true
    }
    LazyColumn(modifier = modifier) {
        item {
            PageHeader(
                title = stringResource(id = R.string.artist),
                isVisibleMore = isVisibleMore,
                onMoreClick = onMoreClick
            )
        }
        items(
            count = min(count, pagingItems.itemCount),
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
                onItemClick(artist.artistId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ArtistListItem(
    artist: ArtistModel,
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
                    .size(68.dp)
                    .clip(CircleShape),
                model = artist.artistImageUrl,
                contentDescription = stringResource(id = R.string.artist)
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