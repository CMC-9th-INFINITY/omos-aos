package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyDjService
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.MyRecord
import com.infinity.omos.data.ResultGetMyDj
import com.infinity.omos.data.ResultGetMyRecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyDjRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    var _djRecord = MutableLiveData<List<MyRecord>>()

    private val testRetrofit: Retrofit = RetrofitAPI.getMovieInstnace()
    private val api = testRetrofit.create(MyRecordService::class.java)
    private val myDjApi = testRetrofit.create(MyDjService::class.java)

    fun getMyDjRecordData(page: Int){
        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                _djRecord.value = response.body()?.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getMyDjData(page: Int): LiveData<List<MyDj>> {
        val data = MutableLiveData<List<MyDj>>()

        myDjApi.getResultMyDj(page).enqueue(object: Callback<ResultGetMyDj> {
            override fun onResponse(
                call: Call<ResultGetMyDj>,
                response: Response<ResultGetMyDj>
            ) {
                data.value = response.body()?.myDjList
            }

            override fun onFailure(call: Call<ResultGetMyDj>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }
}