package com.infinity.omos.api

import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ImageService {
    @DELETE
    fun deleteS3Image(
        @Query("fileName") fileName: String
    ): Call<ResultState>
}