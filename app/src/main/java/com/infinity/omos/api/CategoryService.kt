package com.infinity.omos.api

import com.infinity.omos.data.Record
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {
    @GET("api/records/select/category/{category}")
    fun getCategory(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
        @Query("userid") userid: Int
    ): Call<List<Record>?>
}