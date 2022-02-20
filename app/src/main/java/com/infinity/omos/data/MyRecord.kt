package com.infinity.omos.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  수정 필요
 */

data class MyRecord(
    @SerializedName("title") var title: String,
    @SerializedName("original_title") val contents: String,
    @SerializedName("overview") val category: String,
    @SerializedName("poster_path") val album_cover_path: String
)