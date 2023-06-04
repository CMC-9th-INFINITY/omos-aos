package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.LikeService
import com.infinity.omos.api.FakeRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.api.ScrapService
import com.infinity.omos.data.Record
import com.infinity.omos.data.ResultState
import com.infinity.omos.etc.Constant
import com.infinity.omos.OmosApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val recordApi = retrofit.create(FakeRecordService::class.java)
    private val likeApi = retrofit.create(LikeService::class.java)
    private val scrapApi = retrofit.create(ScrapService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    fun saveScrap(postId: Int, userId: Int){
        scrapApi.saveScrap(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SaveScrapAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("SaveScrapAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        saveLike(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SaveScrapAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("SaveScrapAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("SaveScrapAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun deleteScrap(postId: Int, userId: Int){
        scrapApi.deleteScrap(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DeleteScrapAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("DeleteScrapAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        deleteLike(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DeleteScrapAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("DeleteScrapAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("DeleteScrapAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun saveLike(postId: Int, userId: Int){
        likeApi.saveLike(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SaveLikeAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("SaveLikeAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        saveLike(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SaveLikeAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("SaveLikeAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("SaveLikeAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun deleteLike(postId: Int, userId: Int){
        likeApi.deleteLike(postId, userId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DeleteLikeAPI", "Success: ${body!!.state}")
                    }

                    401 -> {
                        Log.d("DeleteLikeAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        deleteLike(postId, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DeleteLikeAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("DeleteLikeAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("DeleteLikeAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    val detailRecord = MutableLiveData<Record>()
    val stateDetailRecord = MutableLiveData<Constant.ApiState>()
    fun getDetailRecord(postId: Int, userId: Int){
        stateDetailRecord.value = Constant.ApiState.LOADING
        recordApi.getDetailRecord(postId, userId).enqueue(object: Callback<Record> {
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
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
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
                stateDetailRecord.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }
}