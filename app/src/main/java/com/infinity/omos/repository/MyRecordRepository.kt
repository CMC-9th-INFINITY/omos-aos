package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.api.SaveRecordService
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

class MyRecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val myRecordApi = retrofit.create(MyRecordService::class.java)
    private val saveRecordApi = retrofit.create(SaveRecordService::class.java)

    var _myRecord = MutableLiveData<List<Record>?>()

    var _stateSaveRecord = MutableLiveData<ResultState?>()
    var _stateMyRecord = MutableLiveData<Constant.ApiState>()

    fun saveRecord(record: SaveRecord){
        saveRecordApi.saveRecord(record).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SaveRecordAPI", "Success")
                        _stateSaveRecord.value = body
                    }

                    401 -> {
                        Log.d("SaveRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        saveRecord(record)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SaveRecordAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("SaveRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("SaveRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

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