package com.infinity.omos.data.record

data class CategoryModel(
    val category: RecordCategory,
    val records: List<HorizontalPreviewRecordModel>
)