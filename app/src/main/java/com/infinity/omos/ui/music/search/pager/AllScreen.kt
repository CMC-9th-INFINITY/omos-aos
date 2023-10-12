package com.infinity.omos.ui.music.search.pager

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.infinity.omos.ui.theme.OmosTheme

private const val MUSIC_MAX_SIZE = 4
private const val ALBUM_MAX_SIZE = 3
private const val ARTIST_MAX_SIZE = 3

@Composable
fun AllScreen(
    modifier: Modifier = Modifier,
    onMusicMoreClick: () -> Unit = {},
    onAlbumMoreClick: () -> Unit = {},
    onArtistMoreClick: () -> Unit = {}
) {
    Column {
        MusicListScreen(
            itemCount = MUSIC_MAX_SIZE,
            onMusicClick = {},
            onMoreClick = onMusicMoreClick
        )
        AlbumListScreen(
            itemCount = ALBUM_MAX_SIZE,
            onItemClick = {},
            onMoreClick = onAlbumMoreClick
        )
        ArtistListScreen(
            itemCount = ARTIST_MAX_SIZE,
            onItemClick = {},
            onMoreClick = onArtistMoreClick
        )
    }
}

@Preview
@Composable
fun AllScreenPreview() {
    OmosTheme {
        AllScreen()
    }
}