package com.infinity.omos.data.record

import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.utils.DateUtil

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

fun MyRecord.toPresentation(): VerticalRecordModel {
    return VerticalRecordModel(
        musicTitle = music.musicTitle,
        artistName = music.artists.joinToString(separator = ", ") { it.artistName },
        recordId = recordId,
        recordTitle = recordTitle,
        recordContent = recordContents,
        albumImageUrl = music.albumImageUrl,
        dateAndCategory = "${DateUtil.convertToUiRecordDate(createDate)} | ${RecordCategory.valueOf(category).str}"
    )
}