package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.Record
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyRecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val myRecordApi = retrofit.create(MyRecordService::class.java)

    var _myRecord = MutableLiveData<List<Record>?>()
    var _stateMyRecord = MutableLiveData<Constant.ApiState>()

    fun getMyRecord(userId: Int){
        _stateMyRecord.value = Constant.ApiState.LOADING

        myRecordApi.getMyRecord(userId).enqueue(object: Callback<List<Record>> {
            override fun onResponse(
                call: Call<List<Record>>,
                response: Response<List<Record>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyRecordAPI", "Success")
                        _myRecord.value = body
                        _stateMyRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyRecord(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyRecordAPI", errorBody!!.message)
                        _stateMyRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d("MyRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}