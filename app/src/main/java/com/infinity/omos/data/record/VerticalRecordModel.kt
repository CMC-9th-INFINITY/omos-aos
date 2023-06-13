package com.infinity.omos.data.record

data class VerticalRecordModel(
    val musicTitle: String,
    val artistName: String,
    val recordId: Int,
    val recordTitle: String,
    val recordContent: String,
    val albumImageUrl: String,
    val dateAndCategory: String,
    val isPublic: Boolean
)