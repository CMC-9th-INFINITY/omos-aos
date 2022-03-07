package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.AllRecordsService
import com.infinity.omos.api.CategoryService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.Category
import com.infinity.omos.data.DetailCategory
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllRecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val allRecordsApi = retrofit.create(AllRecordsService::class.java)
    private val categoryApi = retrofit.create(CategoryService::class.java)
    var _allRecords = MutableLiveData<Category?>()
    var _category = MutableLiveData<List<DetailCategory>?>()
    var _stateAllRecords = MutableLiveData<Constant.ApiState>()
    var _stateCategory = MutableLiveData<Constant.ApiState>()

    fun setAllRecords(){
        _stateAllRecords.value = Constant.ApiState.LOADING
        allRecordsApi.setAllRecords().enqueue(object: Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AllRecordsAPI", "Success")
                        _allRecords.value = body
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
    }

    fun setCategory(category: String, page: Int, size: Int, sort: String?, userId: Int){
        _stateCategory.value = Constant.ApiState.LOADING
        categoryApi.setCategory(category, page, size, sort, userId).enqueue(object:
            Callback<List<DetailCategory>?> {
            override fun onResponse(
                call: Call<List<DetailCategory>?>,
                response: Response<List<DetailCategory>?>
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
                        setCategory(category, page, size, sort, userId)
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

            override fun onFailure(call: Call<List<DetailCategory>?>, t: Throwable) {
                Log.d("CategoryAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}