package com.infinity.omos.ui.music.search.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.view.OmosTopAppBar
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.Flow

@Composable
fun ArtistScreen(
    viewModel: ArtistViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAlbumClick: (AlbumModel) -> Unit
) {
    ArtistScreen(
        artistName = viewModel.artistName,
        albumStream = viewModel.albumStream,
        onBackClick = onBackClick,
        onAlbumClick = onAlbumClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    artistName: String = "",
    albumStream: Flow<PagingData<AlbumModel>>,
    onBackClick: () -> Unit = {},
    onAlbumClick: (AlbumModel) -> Unit = {}
) {
    val gridState = rememberLazyGridState()
    val albumPagingItems: LazyPagingItems<AlbumModel> = albumStream.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            OmosTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = Dimens.PaddingNormal),
            state = gridState,
            columns = GridCells.Fixed(2)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    modifier = Modifier.padding(bottom = 28.dp),
                    text = artistName,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(
                count = albumPagingItems.itemCount,
                key = { index ->
                    val album = albumPagingItems[index]
                    "${album?.albumId ?: ""}${index}"
                }
            ) { index ->
                val album = albumPagingItems[index] ?: return@items
                AlbumListItem(
                    album = album,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

@Composable
fun AlbumListItem(
    album: AlbumModel,
    onAlbumClick: (AlbumModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                end = Dimens.PaddingNormal,
                bottom = Dimens.PaddingNormal
            )
            .clickable {
            onAlbumClick(album)
        }
    ) {
        GlideImage(
            modifier = Modifier
                .height(160.dp)
                .padding(bottom = 4.dp),
            imageModel = { album.albumImageUrl },
        )
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = album.albumTitle,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = album.releaseDate,
            style = MaterialTheme.typography.labelLarge
        )
    }
}