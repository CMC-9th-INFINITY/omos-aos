package com.infinity.omos.data.record

import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.utils.DateUtil

fun PreviewRecord.toPresentation(): HorizontalPreviewRecordModel {
    return HorizontalPreviewRecordModel(
        music = music.toPresentation(),
        nickname = "by. $nickname",
        recordId = recordId,
        recordImageUrl = recordImageUrl,
        recordTitle = recordTitle,
        userId = userId
    )
}

fun MyRecord.toPresentation(): VerticalPreviewRecordModel {
    return VerticalPreviewRecordModel(
        musicTitle = music.musicTitle,
        artistName = music.artists.joinToString(separator = ", ") { it.artistName },
        recordId = recordId,
        recordTitle = recordTitle,
        recordContent = recordContents,
        albumImageUrl = music.albumImageUrl,
        dateAndCategory = getDateAndCategory(createDate, category),
        isPublic = isPublic
    )
}

fun AllRecords.toPresentation(): List<CategoryModel> {
    return listOf(
        CategoryModel(
            RecordCategory.A_LINE,
            a_line.map { it.toPresentation() }
        ),
        CategoryModel(
            RecordCategory.OST,
            ost.map { it.toPresentation() }
        ),
        CategoryModel(
            RecordCategory.STORY,
            story.map { it.toPresentation() }
        ),
        CategoryModel(
            RecordCategory.LYRICS,
            lyrics.map { it.toPresentation() }
        ),
        CategoryModel(
            RecordCategory.FREE,
            free.map { it.toPresentation() }
        )
    )
}

fun DetailRecord.toPresentation(): DetailRecordModel {
    return DetailRecordModel(
        music = music.toPresentation(),
        recordId = recordId,
        recordImageUrl = recordImageUrl,
        recordTitle = recordTitle,
        recordContents = recordContents,
        userId = userId,
        nickname = "DJ $nickname",
        category = RecordCategory.valueOf(category),
        dateAndCategory = getDateAndCategory(createdDate, category),
        isPublic = isPublic,
        scrapCount = scrapCnt,
        sympathyCount = likeCnt
    )
}

private fun getDateAndCategory(createDate: String, category: String): String =
    "${DateUtil.convertToUiRecordDate(createDate)} | ${RecordCategory.valueOf(category).str}"