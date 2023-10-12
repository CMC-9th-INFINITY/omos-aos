package com.infinity.omos.data.music

import com.infinity.omos.data.music.album.Album
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.data.music.artist.Artist
import com.infinity.omos.data.music.artist.ArtistModel
import com.infinity.omos.utils.DateUtil

fun Music.toPresentation(): MusicModel {
    return MusicModel(
        musicTitle = musicTitle,
        musicId = musicId,
        artists = artists,
        albumTitle = albumTitle,
        albumImageUrl = albumImageUrl,
        artistsAndAlbumTitle = "${artists.joinToString(separator = " & ") { it.artistName }} - $albumTitle"
    )
}

fun Album.toPresentation(): AlbumModel {
    return AlbumModel(
        artists = artists.joinToString(separator = " & ") { it.artistName },
        albumId = albumId,
        albumImageUrl = albumImageUrl,
        albumTitle = albumTitle,
        releaseDate = DateUtil.convertToUiAlbumReleaseDate(releaseDate)
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

fun MusicTitle.toPresentation(keyword: String): MusicTitleModel {
    return MusicTitleModel(
        keyword = keyword,
        title = title
    )
}