package com.infinity.omos.data.record

import com.infinity.omos.data.music.MusicModel

data class DetailRecordModel(
    val music: MusicModel,
    val recordId: Int,
    val recordImageUrl: String,
    val recordTitle: String,
    val recordContents: String,
    val userId: Int,
    val nickname: String,
    val category: RecordCategory,
    val date: String,
    val isPublic: Boolean,
    val scrapCount: Int,
    val sympathyCount: Int,
    val isMine: Boolean,
    val isLiked: Boolean,
    val isScrapped: Boolean
)
