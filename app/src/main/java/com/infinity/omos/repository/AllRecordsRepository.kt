package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.AllRecordsService
import com.infinity.omos.api.CategoryService
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
    private val onBoardingRepository = OnBoardingRepository()

    private val allRecordsApi = retrofit.create(AllRecordsService::class.java)
    private val categoryApi = retrofit.create(CategoryService::class.java)
    var _category = MutableLiveData<List<Record>?>()
    var _stateAllRecords = MutableLiveData<Constant.ApiState>()
    var _stateCategory = MutableLiveData<Constant.ApiState>()

    fun setAllRecords(): MutableLiveData<Category>{
        _stateAllRecords.value = Constant.ApiState.LOADING
        var allRecords = MutableLiveData<Category>()
        allRecordsApi.setAllRecords().enqueue(object: Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AllRecordsAPI", "Success")
                        allRecords.postValue(body!!)
                        _stateAllRecords.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AllRecordsAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        setAllRecords()
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AllRecordsAPI", errorBody!!.message)
                        _stateAllRecords.value = Constant.ApiState.ERROR
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

        return allRecords
    }

    fun getCategory(category: String, page: Int, size: Int, sort: String?, userId: Int){
        _stateCategory.value = Constant.ApiState.LOADING
        categoryApi.getCategory(category, page, size, sort, userId).enqueue(object:
            Callback<List<Record>?> {
            override fun onResponse(
                call: Call<List<Record>?>,
                response: Response<List<Record>?>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("CategoryAPI", "Success")
                        _category.value = body
                        _stateCategory.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("CategoryAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getCategory(category, page, size, sort, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("CategoryAPI", errorBody!!.message)
                        _stateCategory.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("CategoryAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>?>, t: Throwable) {
                Log.d("CategoryAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}