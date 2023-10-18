package com.infinity.omos.data.music.album

import com.infinity.omos.utils.DateUtil

fun Album.toPresentation(): AlbumModel {
    return AlbumModel(
        artists = artists.joinToString(separator = " & ") { it.artistName },
        albumId = albumId,
        albumImageUrl = albumImageUrl,
        albumTitle = albumTitle,
        releaseDate = DateUtil.convertToUiAlbumReleaseDate(releaseDate)
    )
}

fun AlbumMusic.toPresentation(): AlbumMusicModel {
    return AlbumMusicModel(
        artists = artists.joinToString(separator = " & ") { it.artistName },
        musicId = musicId,
        musicTitle = musicTitle
    )
}