package com.infinity.omos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class AllRecords(
    @SerializedName("title") val title: String,
    @SerializedName("myRecord") val myRecord: List<MyRecord>
)