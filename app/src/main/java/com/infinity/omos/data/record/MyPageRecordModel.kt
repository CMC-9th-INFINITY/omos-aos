package com.infinity.omos.data.record

data class MyPageRecordModel(
    val likedRecords: List<HorizontalPreviewRecordModel>,
    val scrappedRecords: List<HorizontalPreviewRecordModel>
)
