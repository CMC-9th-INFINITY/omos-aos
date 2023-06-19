package com.infinity.omos.data.record

import com.infinity.omos.data.music.MusicModel

data class HorizontalPreviewRecordModel(
    val music: MusicModel,
    val nickname: String,
    val recordId: Int,
    val recordImageUrl: String,
    val recordTitle: String,
    val userId: Int
)