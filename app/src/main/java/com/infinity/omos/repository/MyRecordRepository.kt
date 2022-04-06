package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.ImageService
import com.infinity.omos.api.RecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.data.SimpleRecord
import com.infinity.omos.data.Update
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyRecordRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val recordApi = retrofit.create(RecordService::class.java)
    private val imageApi = retrofit.create(ImageService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var stateDeleteImage = MutableLiveData<ResultState>()
    fun deleteImage(directory: String, fileName: String){
        imageApi.deleteS3Image(directory, fileName).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DeleteS3ImageAPI", "Success")
                        stateDeleteImage.postValue(body!!)
                    }

                    401 -> {
                        Log.d("DeleteS3ImageAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        deleteImage(directory, fileName)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DeleteS3ImageAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("DeleteS3ImageAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("DeleteS3ImageAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateUpdateRecord = MutableLiveData<ResultState>()
    fun updateRecord(postId: Int, params: Update){
        recordApi.updateRecord(postId, params).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("UpdateRecordAPI", "Success")
                        stateUpdateRecord.postValue(body!!)
                    }

                    401 -> {
                        Log.d("UpdateRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        updateRecord(postId, params)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("UpdateRecordAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("UpdateRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("UpdateRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateDeleteRecord = MutableLiveData<ResultState>()
    fun deleteRecord(postId: Int){
        recordApi.deleteRecord(postId).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("DeleteRecordAPI", "Success")
                        stateDeleteRecord.postValue(body!!)
                    }

                    401 -> {
                        Log.d("DeleteRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        deleteRecord(postId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("DeleteRecordAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("DeleteRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("DeleteRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateSaveRecord = MutableLiveData<ResultState>()
    fun saveRecord(record: SaveRecord){
        recordApi.saveRecord(record).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SaveRecordAPI", "Success")
                        stateSaveRecord.postValue(body!!)
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

    var myRecord = MutableLiveData<List<SimpleRecord>>()
    var stateMyRecord = MutableLiveData<Constant.ApiState>()
    fun getMyRecord(userId: Int): MutableLiveData<List<SimpleRecord>>{
        stateMyRecord.value = Constant.ApiState.LOADING
        recordApi.getMyRecord(userId).enqueue(object: Callback<List<SimpleRecord>> {
            override fun onResponse(
                call: Call<List<SimpleRecord>>,
                response: Response<List<SimpleRecord>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyRecordAPI", "Success")
                        myRecord.postValue(body!!)
                        stateMyRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyRecord(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyRecordAPI", errorBody!!.message)
                        stateMyRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<SimpleRecord>>, t: Throwable) {
                Log.d("MyRecordAPI", t.message.toString())
                t.stackTrace
            }
        })

        return myRecord
    }
}