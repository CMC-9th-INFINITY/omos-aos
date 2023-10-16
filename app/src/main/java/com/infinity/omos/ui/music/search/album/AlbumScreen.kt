package com.infinity.omos.ui.music.search.album

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.ui.theme.OmosTheme

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val album = AlbumModel(
        artists = viewModel.artists,
        albumId = viewModel.albumId,
        albumImageUrl = viewModel.albumImageUrl,
        albumTitle = viewModel.albumTitle,
        releaseDate = viewModel.releaseDate
    )
    AlbumScreen(
        album = album
    )
}

@Composable
fun AlbumScreen(
    album: AlbumModel
) {
    Text(text = "하이")
}

@Preview
@Composable
fun AlbumScreenPreview() {
    OmosTheme {
        AlbumScreen()
    }
}