package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.*
import com.infinity.omos.data.*
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
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
    private val snsLoginApi = retrofit.create(SnsLoginService::class.java)
    private val snsSignUpApi = retrofit.create(SnsSignUpService::class.java)

    var _status = MutableLiveData<LoginApiState>()
    var _stateDupEmail = MutableLiveData<Boolean>()
    var _stateSns = MutableLiveData<LoginApiState>()
    var _stateSnsSignUp = MutableLiveData<LoginApiState>()

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
                    GlobalApplication.prefs.setString("accessToken", response.body()?.accessToken)
                    GlobalApplication.prefs.setString("refreshToken", response.body()?.refreshToken)
                    GlobalApplication.prefs.setLong("userId", response.body()?.userId!!)
                } else{
                    _status.value = LoginApiState.ERROR
                }
            }

            override fun onFailure(call: Call<ResultGetLogin>, t: Throwable) {
                Log.d("LoginAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun checkSnsLogin(id: UserSNSLogin){
        snsLoginApi.getResultSnsLogin(id).enqueue(object: Callback<ResultGetLogin> {
            override fun onResponse(
                call: Call<ResultGetLogin>,
                response: Response<ResultGetLogin>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("SnsLoginAPI Response", response.body().toString())
                    GlobalApplication.prefs.setString("accessToken", response.body()?.accessToken)
                    GlobalApplication.prefs.setString("refreshToken", response.body()?.refreshToken)
                    GlobalApplication.prefs.setLong("userId", response.body()?.userId!!)
                    _stateSns.value = LoginApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("SnsLoginAPI Error", errorBody!!.message)

                    if (errorBody.message == "해당하는 유저가 존재하지 않습니다"){
                        _stateSns.value = LoginApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<ResultGetLogin>, t: Throwable) {
                Log.d("SnsLoginAPI Failure", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun signUp(){

    }

    fun snsSignUp(userInfo: UserSnsSignUp){
        snsSignUpApi.getResultSnsSignUp(userInfo).enqueue(object: Callback<ResultGetLogin>{
            override fun onResponse(
                call: Call<ResultGetLogin>,
                response: Response<ResultGetLogin>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("signUpAPI Response", response.body().toString())
                    GlobalApplication.prefs.setString("accessToken", response.body()?.accessToken)
                    GlobalApplication.prefs.setString("refreshToken", response.body()?.refreshToken)
                    GlobalApplication.prefs.setLong("userId", response.body()?.userId!!)
                    _stateSnsSignUp.value = LoginApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("signUpAPI Error", errorBody!!.message)

                    if (errorBody.message == "이미 있는 닉네임입니다"){
                        _stateSnsSignUp.value = LoginApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<ResultGetLogin>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun checkDupEmail(email: String){
        checkDupApi.checkDupEmail(email).enqueue(object: Callback<ResultCheckEmail>{
            override fun onResponse(
                call: Call<ResultCheckEmail>,
                response: Response<ResultCheckEmail>
            ) {
                Log.d("checkDupEmailAPI", response.body()?.state.toString())
                _stateDupEmail.value = response.body()?.state
            }

            override fun onFailure(call: Call<ResultCheckEmail>, t: Throwable) {
                Log.d("checkDupEmailAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}