package com.infinity.omos.data.music

import com.infinity.omos.data.Artists

data class MusicModel(
    val musicId: String,
    val musicTitle: String,
    val artists: List<Artists>,
    val albumTitle: String,
    val albumImageUrl: String,
    val artistsAndAlbumTitle: String
)