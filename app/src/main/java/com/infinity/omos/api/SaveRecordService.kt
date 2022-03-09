package com.infinity.omos.api

import com.infinity.omos.data.Record
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SaveRecord
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SaveRecordService {
    @POST("api/records/save")
    fun saveRecord(
        @Body params: SaveRecord
    ): Call<ResultState>
}