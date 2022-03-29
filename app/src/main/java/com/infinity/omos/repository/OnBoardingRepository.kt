package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.AuthService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class OnBoardingRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingApi = retrofit.create(AuthService::class.java)

    var stateGetCode = MutableLiveData<Constant.ApiState>()
    var emailCode = MutableLiveData<String>()
    fun getEmailCode(email: String){
        stateGetCode.value = Constant.ApiState.LOADING
        onBoardingApi.getEmailCode(Email(email)).enqueue(object: Callback<Code> {
            override fun onResponse(
                call: Call<Code>,
                response: Response<Code>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("GetEmailCodeAPI", "Success")
                        emailCode.postValue(body?.code)
                        stateGetCode.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("GetEmailCodeAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("GetEmailCodeAPI", errorBody!!.message)
                        stateGetCode.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("GetEmailCodeAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<Code>, t: Throwable) {
                Log.d("GetEmailCodeAPI", t.message.toString())
                stateGetCode.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    var stateToken = MutableLiveData<Constant.ApiState>()
    fun getUserToken(userInfo: UserToken){
        stateToken.value = Constant.ApiState.LOADING
        onBoardingApi.getToken(userInfo).enqueue(object: Callback<UserToken> {
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ReissueAPI", "Save Token")
                        GlobalApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)

                        stateToken.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ReissueAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ReissueAPI", errorBody!!.message)
                        stateToken.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ReissueAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("ReissueAPI", t.message.toString())
                stateToken.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    var stateLogin = MutableLiveData<Constant.ApiState>()
    fun checkLogin(userLogin: UserLogin){
        onBoardingApi.getResultLogin(userLogin).enqueue(object: Callback<UserToken> {
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("LoginAPI", "Success Login")
                        GlobalApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)

                        stateLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("LoginAPI", "Unauthorized")
                        stateLogin.value = Constant.ApiState.ERROR
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("LoginAPI", errorBody!!.message)
                        stateLogin.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("LoginAPI", "Code: $code")
                        stateLogin.value = Constant.ApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("LoginAPI", t.message.toString())
                stateLogin.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    var stateSnsLogin = MutableLiveData<Constant.ApiState>()
    fun checkSnsLogin(id: UserSnsLogin){
        onBoardingApi.getResultSnsLogin(id).enqueue(object: Callback<UserToken> {
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SnsLoginAPI", "Success Login")
                        GlobalApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)

                        stateSnsLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("SnsLoginAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SnsLoginAPI", errorBody!!.message)
                        if (errorBody.message == "해당하는 유저가 존재하지 않습니다"){
                            stateSnsLogin.value = Constant.ApiState.ERROR
                        }
                    }

                    else -> {
                        Log.d("SnsLoginAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("SnsLoginAPI", t.message.toString())
                stateSnsLogin.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    var stateSignUp = MutableLiveData<Constant.ApiState>()
    fun signUp(userInfo: UserSignUp){
        onBoardingApi.getResultSignUp(userInfo).enqueue(object: Callback<ResultState>{
            override fun onResponse(call: Call<ResultState>, response: Response<ResultState>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("signUpAPI", "Success SignUp")
                        if(body?.state == true){
                            stateSignUp.value = Constant.ApiState.DONE
                        } else{
                            stateSignUp.value = Constant.ApiState.ERROR
                        }
                    }

                    401 -> {
                        Log.d("signUpAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("signUpAPI", errorBody!!.message)
                        if (errorBody.message == "이미 있는 닉네임입니다"){
                            stateSignUp.value = Constant.ApiState.ERROR
                        }
                    }

                    else -> {
                        Log.d("signUpAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateSnsSignUp = MutableLiveData<Constant.ApiState>()
    fun snsSignUp(userInfo: UserSnsSignUp){
        onBoardingApi.getResultSnsSignUp(userInfo).enqueue(object: Callback<UserToken>{
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("signUpAPI", "Success SignUp")
                        GlobalApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)
                        stateSnsSignUp.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("signUpAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("signUpAPI", errorBody!!.message)
                        if (errorBody.message == "이미 있는 닉네임입니다"){
                            stateSnsSignUp.value = Constant.ApiState.ERROR
                        }
                    }

                    else -> {
                        Log.d("signUpAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateDupEmail = MutableLiveData<Boolean>()
    fun checkDupEmail(email: String){
        onBoardingApi.checkDupEmail(email).enqueue(object: Callback<ResultState>{
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("checkDupEmailAPI", "Success")
                        stateDupEmail.postValue(body?.state)
                    }

                    401 -> {
                        Log.d("checkDupEmailAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("checkDupEmailAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("checkDupEmailAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("checkDupEmailAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}