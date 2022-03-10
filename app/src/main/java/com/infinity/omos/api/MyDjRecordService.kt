package com.infinity.omos.api

import com.infinity.omos.data.Profile
import com.infinity.omos.data.Record
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyDjRecordService {
    @GET("api/records/select/user/{fromUserId}/{toUserId}")
    fun getMyDjRecord(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<List<Record>>
}