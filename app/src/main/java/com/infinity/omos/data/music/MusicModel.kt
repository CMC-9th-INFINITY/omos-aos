package com.infinity.omos.data.music

data class MusicModel(
    val musicId: String,
    val musicTitle: String,
    val artists: String,
    val albumTitle: String,
    val albumImageUrl: String,
    val artistsAndAlbumTitle: String
)