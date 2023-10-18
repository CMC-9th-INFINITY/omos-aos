package com.infinity.omos.ui.music.search.album

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.data.music.album.AlbumMusicModel
import com.infinity.omos.ui.theme.OmosTheme

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = hiltViewModel()
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
        musicList = musicList
    )
}

@Composable
fun AlbumScreen(
    album: AlbumModel,
    musicList: List<AlbumMusicModel>
) {
    Text(
        text = album.albumTitle,
        style = MaterialTheme.typography.bodyLarge
    )
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