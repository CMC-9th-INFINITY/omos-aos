package com.infinity.omos.data.music.artist

data class ArtistModel(
    val artistId: String,
    val artistImageUrl: String = "",
    val artistName: String,
    val genres: String,
)