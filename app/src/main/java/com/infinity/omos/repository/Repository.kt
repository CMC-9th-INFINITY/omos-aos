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
import com.infinity.omos.etc.Constant.*

/**
 *  수정 필요
 */
class Repository {

    /**
     *  추후 삭제 요망
     */
    private val testRetrofit: Retrofit = RetrofitAPI.getMovieInstnace()
    private val api = testRetrofit.create(MyRecordService::class.java)
    private val myDjApi = testRetrofit.create(MyDjService::class.java)

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()

    // My 레코드
    var _myRecord = MutableLiveData<List<MyRecord>>()
    var _stateMyRecord = MutableLiveData<ApiState>()

    // 전체 레코드
    private val allRecordsApi = retrofit.create(AllRecordsService::class.java)
    private val categoryApi = retrofit.create(CategoryService::class.java)
    var _allRecords = MutableLiveData<Category?>()
    var _category = MutableLiveData<List<DetailCategory>?>()
    var _stateAllRecords = MutableLiveData<ApiState>()
    var _stateCategory = MutableLiveData<ApiState>()

    // My DJ
    var _djRecord = MutableLiveData<List<MyRecord>>()

    private val onBoardingRepository = OnBoardingRepository()

    fun setCategory(category: String, page: Int, size: Int, sort: String?, userId: Int){
        _stateCategory.value = ApiState.LOADING
        categoryApi.setCategory(category, page, size, sort, userId).enqueue(object: Callback<List<DetailCategory>?>{
            override fun onResponse(
                call: Call<List<DetailCategory>?>,
                response: Response<List<DetailCategory>?>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("CategoryAPI Response", response.body().toString())
                    _category.value = body
                    _stateCategory.value = ApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("CategoryAPI Error", errorBody!!.message)

                    if (errorBody.message == "Refresh Token이 유효하지 않습니다."){
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        _stateCategory.value = ApiState.TOKEN
                    }
                }
            }

            override fun onFailure(call: Call<List<DetailCategory>?>, t: Throwable) {
                Log.d("CategoryAPI Failure", t.message.toString())
                _stateCategory.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun setAllRecords(){
        _stateAllRecords.value = ApiState.LOADING
        allRecordsApi.setAllRecords().enqueue(object: Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AllRecordsAPI Response", response.body().toString())
                        _allRecords.value = body
                        _stateAllRecords.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        _stateAllRecords.value = ApiState.TOKEN
                    }

                    else -> {
                        Log.d("Repository", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("AllRecordsAPI Failure", t.message.toString())
                _stateAllRecords.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getTestData(page: Int): LiveData<List<MyRecord>>{
        val data = MutableLiveData<List<MyRecord>>()

        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                data.value = response.body()?.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }

    fun getMyRecordData(page: Int){
        _stateMyRecord.value = ApiState.LOADING

        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                _myRecord.value = response.body()?.myRecordList
                _stateMyRecord.value = ApiState.DONE
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                _stateMyRecord.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getMyDjRecordData(page: Int){
        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                _djRecord.value = response.body()?.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getMyDjData(page: Int): LiveData<List<MyDj>>{
        val data = MutableLiveData<List<MyDj>>()

        myDjApi.getResultMyDj(page).enqueue(object: Callback<ResultGetMyDj> {
            override fun onResponse(
                call: Call<ResultGetMyDj>,
                response: Response<ResultGetMyDj>
            ) {
                data.value = response.body()?.myDjList
            }

            override fun onFailure(call: Call<ResultGetMyDj>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }
}