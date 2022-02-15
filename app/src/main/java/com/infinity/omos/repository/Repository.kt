package com.infinity.omos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.MyRecord
import com.infinity.omos.data.ResultGetMyRecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 *  수정 필요
 */
class Repository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val api = retrofit.create(MyRecordService::class.java)

    fun getMyRecordData(page: Int): LiveData<List<MyRecord>>{
        val data = MutableLiveData<List<MyRecord>>()

        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                data.value = response.body()!!.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                t.stackTrace
            }
        })

        return data
    }
}