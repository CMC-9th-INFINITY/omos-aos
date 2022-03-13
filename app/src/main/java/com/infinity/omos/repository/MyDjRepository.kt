package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.*
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
    private val followApi = retrofit.create(FollowService::class.java)
    private val myDjRecordApi = retrofit.create(RecordService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var myDjAllRecords = MutableLiveData<List<Record>>()
    var stateMyDjAllRecords = MutableLiveData<Constant.ApiState>()
    fun getMyDjAllRecords(userId: Int, postId: Int?, size: Int){
        stateMyDjAllRecords.value = Constant.ApiState.LOADING
        myDjRecordApi.getMyDjAllRecords(userId, postId, size).enqueue(object: Callback<List<Record>> {
            override fun onResponse(
                call: Call<List<Record>>,
                response: Response<List<Record>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyDjAllRecordsAPI", "Success")
                        myDjAllRecords.postValue(body!!)
                        stateMyDjAllRecords.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyDjAllRecordsAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyDjAllRecords(userId, postId, size)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyDjAllRecordsAPI", errorBody!!.message)
                        stateMyDjAllRecords.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyDjAllRecordsAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d("MyDjAllRecordsAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun deleteFollow(postId: Int, userId: Int){
        followApi.deleteFollow(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DeleteFollowAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("DeleteFollowAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        deleteFollow(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DeleteFollowAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("DeleteFollowAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("DeleteFollowAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun saveFollow(postId: Int, userId: Int){
        followApi.saveFollow(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SaveFollowAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("SaveFollowAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        saveFollow(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SaveFollowAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("SaveFollowAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("SaveFollowAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var djProfile = MutableLiveData<DjProfile>()
    var stateDjProfile = MutableLiveData<Constant.ApiState>()
    fun getDjProfile(fromUserId: Int, toUserId: Int){
        stateDjProfile.value = Constant.ApiState.LOADING
        followApi.getDjProfile(fromUserId, toUserId).enqueue(object: Callback<DjProfile> {
            override fun onResponse(
                call: Call<DjProfile>,
                response: Response<DjProfile>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DjProfileAPI", "Success")
                        djProfile.postValue(body!!)
                        stateDjProfile.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("DjProfileAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getDjProfile(fromUserId, toUserId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DjProfileAPI", errorBody!!.message)
                        stateDjProfile.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("DjProfileAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<DjProfile>, t: Throwable) {
                Log.d("DjProfileAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

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

    var stateMyDj = MutableLiveData<Constant.ApiState>()
    fun getMyDj(userId: Int): LiveData<List<Profile>?>{
        var data = MutableLiveData<List<Profile>?>()

        stateMyDj.value = Constant.ApiState.LOADING
        followApi.getMyDj(userId).enqueue(object: Callback<List<Profile>> {
            override fun onResponse(
                call: Call<List<Profile>>,
                response: Response<List<Profile>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyDjAPI", "Success")
                        data.value = body
                        stateMyDj.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyDjAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyDj(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyDjAPI", errorBody!!.message)
                        stateMyDj.value = Constant.ApiState.ERROR
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