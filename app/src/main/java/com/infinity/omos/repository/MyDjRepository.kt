package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyDjRecordService
import com.infinity.omos.api.MyDjService
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyDjRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val myDjApi = retrofit.create(MyDjService::class.java)
    private val myDjRecordApi = retrofit.create(MyDjRecordService::class.java)
    var _stateMyDj = MutableLiveData<Constant.ApiState>()


    var myDjRecord = MutableLiveData<List<Record>>()
    var stateMyDjRecord = MutableLiveData<Constant.ApiState>()
    fun getMyDjRecord(fromUserId: Int, toUserId: Int){
        stateMyDjRecord.value = Constant.ApiState.LOADING

        myDjRecordApi.getMyDjRecord(fromUserId, toUserId).enqueue(object: Callback<List<Record>> {
            override fun onResponse(
                call: Call<List<Record>>,
                response: Response<List<Record>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyDjRecordAPI", "Success")
                        myDjRecord.postValue(body!!)
                        stateMyDjRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyDjRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyDjRecord(fromUserId, toUserId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyDjRecordAPI", errorBody!!.message)
                        stateMyDjRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyDjRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d("MyDjRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getMyDj(userId: Int): LiveData<List<Profile>?>{
        var data = MutableLiveData<List<Profile>?>()

        _stateMyDj.value = Constant.ApiState.LOADING
        myDjApi.getMyDj(userId).enqueue(object: Callback<List<Profile>> {
            override fun onResponse(
                call: Call<List<Profile>>,
                response: Response<List<Profile>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyDjAPI", "Success")
                        data.value = body
                        _stateMyDj.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyDjAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyDj(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyDjAPI", errorBody!!.message)
                        _stateMyDj.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyDjAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
                Log.d("MyDjAPI", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }
}