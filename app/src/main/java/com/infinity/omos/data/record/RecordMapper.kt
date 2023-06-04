package com.infinity.omos.data.record

import com.infinity.omos.data.music.toPresentation

fun SumRecord.toPresentation(): HorizontalRecordModel {
    return HorizontalRecordModel(
        music = music.toPresentation(),
        nickname = "by. $nickname",
        recordId = recordId,
        recordImageUrl = recordImageUrl,
        recordTitle = recordTitle,
        userId = userId
    )
}