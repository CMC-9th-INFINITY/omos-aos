package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.MyPageService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.SimpleRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyPageRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val myPageApi = retrofit.create(MyPageService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var scrapRecord = MutableLiveData<List<SimpleRecord>>()
    var stateScrapRecord = MutableLiveData<Constant.ApiState>()
    fun getScrapRecord(userId: Int){
        stateScrapRecord.value = Constant.ApiState.LOADING
        myPageApi.getScrapRecord(userId).enqueue(object:
            Callback<List<SimpleRecord>> {
            override fun onResponse(
                call: Call<List<SimpleRecord>>,
                response: Response<List<SimpleRecord>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ScrapRecordAPI", "Success")
                        scrapRecord.postValue(body!!)
                        stateScrapRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ScrapRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getScrapRecord(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ScrapRecordAPI", errorBody!!.message)
                        stateScrapRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ScrapRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<SimpleRecord>>, t: Throwable) {
                Log.d("ScrapRecordAPI", t.message.toString())
                stateScrapRecord.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    var likeRecord = MutableLiveData<List<SimpleRecord>>()
    var stateLikeRecord = MutableLiveData<Constant.ApiState>()
    fun getLikeRecord(userId: Int){
        stateLikeRecord.value = Constant.ApiState.LOADING
        myPageApi.getLikeRecord(userId).enqueue(object:
            Callback<List<SimpleRecord>> {
            override fun onResponse(
                call: Call<List<SimpleRecord>>,
                response: Response<List<SimpleRecord>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("LikeRecordAPI", "Success")
                        likeRecord.postValue(body!!)
                        stateLikeRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("LikeRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getLikeRecord(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("LikeRecordAPI", errorBody!!.message)
                        stateLikeRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("LikeRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<SimpleRecord>>, t: Throwable) {
                Log.d("LikeRecordAPI", t.message.toString())
                stateLikeRecord.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }
}