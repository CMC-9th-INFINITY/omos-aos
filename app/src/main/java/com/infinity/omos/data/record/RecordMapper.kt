package com.infinity.omos.data.record

import com.infinity.omos.data.music.toPresentation

fun SumRecord.toPresentation(): SumRecordModel {
    return SumRecordModel(
        music = music.toPresentation(),
        nickname = nickname,
        recordId = recordId,
        recordImageUrl = recordImageUrl,
        recordTitle = recordTitle,
        userId = userId
    )
}