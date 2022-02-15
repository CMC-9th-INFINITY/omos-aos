package com.infinity.omos.data

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class AllRecords(
    @SerializedName("title") val title: String,
    @SerializedName("myRecord") var myRecord: List<MyRecord>
)