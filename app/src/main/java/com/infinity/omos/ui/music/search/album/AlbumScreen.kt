package com.infinity.omos.ui.music.search.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.R
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.data.music.album.AlbumMusicModel
import com.infinity.omos.ui.Dimens
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.ui.theme.grey_01
import com.infinity.omos.ui.theme.grey_02
import com.infinity.omos.ui.theme.grey_03
import com.infinity.omos.ui.view.OmosTopAppBar
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onMusicClick: (String) -> Unit
) {
    val musicList = viewModel.musicList.collectAsState().value
    val album = AlbumModel(
        artists = viewModel.artists,
        albumId = viewModel.albumId,
        albumImageUrl = viewModel.albumImageUrl,
        albumTitle = viewModel.albumTitle,
        releaseDate = viewModel.releaseDate
    )

    AlbumScreen(
        album = album,
        musicList = musicList,
        onBackClick = onBackClick,
        onMusicClick = onMusicClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    album: AlbumModel,
    musicList: List<AlbumMusicModel>,
    onBackClick: () -> Unit = {},
    onMusicClick: (String) -> Unit = {}
) {
    val lazyListState = rememberLazyListState()

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    modifier = Modifier.padding(bottom = 20.dp),
                    painter = painterResource(id = R.drawable.logo_spotify),
                    contentDescription = stringResource(id = R.string.spotify_logo)
                )
                GlideImage(
                    modifier = Modifier
                        .size(200.dp),
                    imageModel = { album.albumImageUrl },
                    imageOptions = ImageOptions(contentScale = ContentScale.Fit)
                )
                Text(
                    modifier = Modifier.padding(
                        top = 18.dp,
                        bottom = 2.dp
                    ),
                    text = album.albumTitle,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = album.artists,
                    style = MaterialTheme.typography.bodyMedium,
                    color = grey_02
                )
                Text(
                    modifier = Modifier.padding(bottom = 32.dp),
                    text = album.releaseDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = grey_03
                )
            }
            itemsIndexed(
                items = musicList,
                key = { _, music ->
                    music.musicId
                }
            ) { idx, music ->
                AlbumMusicListItem(
                    music = music,
                    idx = idx,
                    onMusicClick = onMusicClick
                )
            }
        }
    }
}

@Composable
fun AlbumMusicListItem(
    music: AlbumMusicModel,
    idx: Int,
    onMusicClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMusicClick(music.musicId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
                .align(Alignment.CenterVertically),
            text = idx.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = grey_01
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = music.musicTitle,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = music.artists,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Image(
            modifier = Modifier
                .padding(Dimens.PaddingNormal)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.write)
        )
    }
}

@Preview
@Composable
fun AlbumScreenPreview(
    @PreviewParameter(AlbumScreenPreviewParamProvider::class) musicList: List<AlbumMusicModel>
) {
    OmosTheme {
        val album = AlbumModel(
            artists = "가수 이름",
            albumId = "0",
            albumImageUrl = "",
            albumTitle = "앨범 타이틀",
            releaseDate = "2023.01.01"
        )

        AlbumScreen(
            album = album,
            musicList = musicList
        )
    }
}

private class AlbumScreenPreviewParamProvider : PreviewParameterProvider<List<AlbumMusicModel>> {
    override val values: Sequence<List<AlbumMusicModel>> =
        sequenceOf(
            listOf(
                AlbumMusicModel(
                    artists = "가수 이름 1",
                    musicId = "1",
                    musicTitle = "노래 제목 1"
                ),
                AlbumMusicModel(
                    artists = "가수 이름 2",
                    musicId = "2",
                    musicTitle = "노래 제목 2"
                ),
                AlbumMusicModel(
                    artists = "가수 이름 3",
                    musicId = "3",
                    musicTitle = "노래 제목 3"
                )
            )
        )
}