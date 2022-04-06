package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.BlockService
import com.infinity.omos.api.RecordService
import com.infinity.omos.api.ReportService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.ReportBlock
import com.infinity.omos.data.ResultState
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ReportBlockRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val reportApi = retrofit.create(ReportService::class.java)
    private val blockApi = retrofit.create(BlockService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var stateReportObject = MutableLiveData<ResultState>()
    fun reportObject(fromUserId: Int, recordId: Int?, reportReason: String?, toUserId: Int?, type: String){
        reportApi.reportObject(ReportBlock(fromUserId, recordId, reportReason, toUserId), type).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ReportAPI", "Success")
                        stateReportObject.postValue(body!!)
                    }

                    401 -> {
                        Log.d("ReportAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        reportObject(fromUserId, recordId, reportReason, toUserId, type)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ReportAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("ReportAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("ReportAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateBlockUser = MutableLiveData<ResultState>()
    fun blockUser(fromUserId: Int, recordId: Int, reportReason: String, toUserId: Int, type: String){
        blockApi.blockUser(ReportBlock(fromUserId, recordId, reportReason, toUserId), type).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("BlockAPI", "Success")
                        stateBlockUser.postValue(body!!)
                    }

                    401 -> {
                        Log.d("BlockAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        blockUser(fromUserId, recordId, reportReason, toUserId, type)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("BlockAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("BlockAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("BlockAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}