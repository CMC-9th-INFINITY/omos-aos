package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.RecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.Category
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllRecordsRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val recordApi = retrofit.create(RecordService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var stateAllRecords = MutableLiveData<Constant.ApiState>()
    var allRecords = MutableLiveData<Category>()
    fun setAllRecords(userId: Int){
        stateAllRecords.value = Constant.ApiState.LOADING
        recordApi.setAllRecords(userId).enqueue(object: Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AllRecordsAPI", "Success")
                        allRecords.postValue(body!!)
                        stateAllRecords.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AllRecordsAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        setAllRecords(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AllRecordsAPI", errorBody!!.message)
                        stateAllRecords.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("AllRecordsAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("AllRecordsAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var _category = MutableLiveData<List<Record>>()
    var stateCategory = MutableLiveData<Constant.ApiState>()
    fun getCategory(category: String, postId: Int?, size: Int, sortType: String, userId: Int){
        stateCategory.value = Constant.ApiState.LOADING
        recordApi.getCategory(category, postId, size, sortType, userId).enqueue(object:
            Callback<List<Record>> {
            override fun onResponse(
                call: Call<List<Record>>,
                response: Response<List<Record>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("CategoryAPI", "Success")
                        _category.postValue(body!!)
                        stateCategory.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("CategoryAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getCategory(category, postId, size, sortType, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("CategoryAPI", errorBody!!.message)
                        stateCategory.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("CategoryAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d("CategoryAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}