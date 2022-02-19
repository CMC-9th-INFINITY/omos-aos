package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.LoginActivity.Companion.userToken
import com.infinity.omos.api.DupEmailService
import com.infinity.omos.api.LoginService
import com.infinity.omos.api.MyRecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 *  수정 필요
 */
class Repository {

    /**
     *  추후 삭제 요망
     */
    private val testRetrofit: Retrofit = RetrofitAPI.getMovieInstnace()
    private val api = testRetrofit.create(MyRecordService::class.java)

    enum class LoginApiState{LOADING, ERROR, DONE}

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val loginApi = retrofit.create(LoginService::class.java)
    private val checkDupApi = retrofit.create(DupEmailService::class.java)

    var _status = MutableLiveData<LoginApiState>()
    var _stateDupEmail = MutableLiveData<Boolean>()

    fun getMyRecordData(page: Int): LiveData<List<MyRecord>>{
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

    fun checkLogin(userLogin: UserLogin){
        loginApi.getResultGetLogin(userLogin).enqueue(object: Callback<ResultGetLogin> {
            override fun onResponse(
                call: Call<ResultGetLogin>,
                response: Response<ResultGetLogin>
            ) {
                Log.d("LoginAPI", response.body().toString())
                if (response.body() != null){
                    _status.value = LoginApiState.DONE
                    userToken = UserToken(
                        response.body()?.accessToken,
                        response.body()?.refreshToken,
                        response.body()?.userId
                    )
                } else{
                    _status.value = LoginApiState.ERROR
                }
            }

            override fun onFailure(call: Call<ResultGetLogin>, t: Throwable) {
                Log.d("checkLoginAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun checkDupEmail(email: String){
        checkDupApi.checkDupEmail(email).execute()
        checkDupApi.checkDupEmail(email).enqueue(object: Callback<ResultCheckEmail>{
            override fun onResponse(
                call: Call<ResultCheckEmail>,
                response: Response<ResultCheckEmail>
            ) {
                _stateDupEmail.value = response.body()?.state
            }

            override fun onFailure(call: Call<ResultCheckEmail>, t: Throwable) {
                Log.d("checkDupEmailAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}