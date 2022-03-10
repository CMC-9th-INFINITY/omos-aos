package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.DetailRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.Record
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val detailRecordApi = retrofit.create(DetailRecordService::class.java)

    val detailRecord = MutableLiveData<Record>()
    val stateDetailRecord = MutableLiveData<Constant.ApiState>()
    fun getDetailRecord(postId: Int, userId: Int){
        stateDetailRecord.value = Constant.ApiState.LOADING
        detailRecordApi.getDetailRecord(postId, userId).enqueue(object: Callback<Record> {
            override fun onResponse(
                call: Call<Record>,
                response: Response<Record>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DetailRecordAPI", "Success")
                        detailRecord.postValue(body!!)
                        stateDetailRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("DetailRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getDetailRecord(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DetailRecordAPI", errorBody!!.message)
                        stateDetailRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("DetailRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<Record>, t: Throwable) {
                Log.d("DetailRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}