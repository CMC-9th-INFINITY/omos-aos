package com.infinity.omos.data.music

fun Music.toPresentation(): MusicModel {
    return MusicModel(
        musicTitle = musicTitle,
        musicId = musicId,
        artists = artists,
        albumTitle = albumTitle,
        albumImageUrl = albumImageUrl,
        artistsAndAlbumTitle = "${artists.joinToString(separator = ", ") { it.artistName }} - $albumTitle"
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