package com.infinity.omos.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    private const val REMOTE_RECORD_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    private const val UI_RECORD_DATE_PATTERN = "yyyy/MM/dd"

    private const val REMOTE_ALBUM_RELEASE_DATE_PATTERN = "yyyy-MM-dd"
    private const val UI_ALBUM_RELEASE_DATE_PATTERN = "yyyy.MM.dd"

    private val remoteRecordDateFormat = SimpleDateFormat(REMOTE_RECORD_DATE_PATTERN, Locale.KOREA)
    private val uiRecordDateFormat = SimpleDateFormat(UI_RECORD_DATE_PATTERN, Locale.KOREA)

    private val remoteAlbumReleaseDateFormat = SimpleDateFormat(REMOTE_ALBUM_RELEASE_DATE_PATTERN, Locale.KOREA)
    private val uiAlbumReleaseDateFormat = SimpleDateFormat(UI_ALBUM_RELEASE_DATE_PATTERN, Locale.KOREA)

    fun convertToUiRecordDate(str: String): String {
        val date = remoteRecordDateFormat.parse(str)
        return if (date != null) {
            uiRecordDateFormat.format(date)
        } else {
            "DATE ERROR"
        }
    }

    fun convertToUiAlbumReleaseDate(str: String): String {
        val date = remoteAlbumReleaseDateFormat.parse(str)
        return if (date != null) {
            uiAlbumReleaseDateFormat.format(date)
        } else {
            "DATE ERROR"
        }
    }
}