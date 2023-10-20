package com.infinity.omos.data.music

import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.artist.ArtistModel

fun Music.toPresentation(): MusicModel {
    return MusicModel(
        musicTitle = musicTitle,
        musicId = musicId,
        artists = artists.joinToString(separator = " & ") { it.artistName },
        albumTitle = albumTitle,
        albumImageUrl = albumImageUrl,
        artistsAndAlbumTitle = "${artists.joinToString(separator = " & ") { it.artistName }} - $albumTitle"
    )
}

fun Artist.toPresentation(): ArtistModel {
    return ArtistModel(
        artistId = artistId,
        artistImageUrl = artistImageUrl ?: "",
        artistName = artistName,
        genres = genres.joinToString(" & ")
    )
}

fun LovedMusic.toPresentation(): LovedMusicModel {
    return LovedMusicModel(
        recordId = recordId,
        music = music.toPresentation(),
        recordImageUrl = recordImageUrl
    )
}

fun MusicTitle.toPresentation(): MusicTitleModel {
    return MusicTitleModel(
        title = title
    )
}