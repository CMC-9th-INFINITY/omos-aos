package com.infinity.omos.factory

import com.infinity.omos.data.music.MusicModel

fun createEmptyMusicModel() = MusicModel(
    musicId = "",
    musicTitle = "",
    artists = "",
    albumTitle = "",
    albumImageUrl = "",
    artistsAndAlbumTitle = ""
)