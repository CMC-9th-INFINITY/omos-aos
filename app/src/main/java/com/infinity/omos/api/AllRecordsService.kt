package com.infinity.omos.api

import com.infinity.omos.data.Category
import com.infinity.omos.utils.GlobalApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface AllRecordsService {
    @GET("api/records/select")
    fun getAllRecords(): Call<Category>
}