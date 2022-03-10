package com.infinity.omos.repository

import android.util.Log
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

class OnBoardingRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()

    private val loginApi = retrofit.create(LoginService::class.java)
    private val signUpApi = retrofit.create(SignUpService::class.java)
    private val snsLoginApi = retrofit.create(SnsLoginService::class.java)
    private val snsSignUpApi = retrofit.create(SnsSignUpService::class.java)
    private val checkDupApi = retrofit.create(DupEmailService::class.java)
    private val reissueApi = retrofit.create(ReissueService::class.java)
    var _stateLogin = MutableLiveData<Constant.ApiState>()
    var _stateSignUp = MutableLiveData<Constant.ApiState>()
    var _stateSnsLogin = MutableLiveData<Constant.ApiState>()
    var _stateSnsSignUp = MutableLiveData<Constant.ApiState>()
    var _stateDupEmail = MutableLiveData<Boolean>()
    var _stateToken = MutableLiveData<Constant.ApiState>()

    fun getUserToken(userInfo: UserToken){
        _stateToken.value = Constant.ApiState.LOADING
        reissueApi.getToken(userInfo).enqueue(object: Callback<UserToken> {
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

                        _stateToken.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ReissueAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ReissueAPI", errorBody!!.message)
                        _stateToken.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ReissueAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("ReissueAPI", t.message.toString())
                _stateToken.value = Constant.ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun checkLogin(userLogin: UserLogin){
        loginApi.getResultLogin(userLogin).enqueue(object: Callback<UserToken> {
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

                        _stateLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("LoginAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("LoginAPI", errorBody!!.message)
                        _stateLogin.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("LoginAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("LoginAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun checkSnsLogin(id: UserSnsLogin){
        snsLoginApi.getResultSnsLogin(id).enqueue(object: Callback<UserToken> {
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

                        _stateSnsLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("SnsLoginAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SnsLoginAPI", errorBody!!.message)
                        if (errorBody.message == "해당하는 유저가 존재하지 않습니다"){
                            _stateSnsLogin.value = Constant.ApiState.ERROR
                        }
                    }

                    else -> {
                        Log.d("SnsLoginAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("SnsLoginAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun signUp(userInfo: UserSignUp){
        signUpApi.getResultSignUp(userInfo).enqueue(object: Callback<ResultState>{
            override fun onResponse(call: Call<ResultState>, response: Response<ResultState>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("signUpAPI", "Success SignUp")
                        if(body?.state == true){
                            _stateSignUp.value = Constant.ApiState.DONE
                        } else{
                            _stateSignUp.value = Constant.ApiState.ERROR
                        }
                    }

                    401 -> {
                        Log.d("signUpAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("signUpAPI", errorBody!!.message)
                        if (errorBody.message == "이미 있는 닉네임입니다"){
                            _stateSignUp.value = Constant.ApiState.ERROR
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

    fun snsSignUp(userInfo: UserSnsSignUp){
        snsSignUpApi.getResultSnsSignUp(userInfo).enqueue(object: Callback<UserToken>{
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
                        _stateSnsSignUp.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("signUpAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("signUpAPI", errorBody!!.message)
                        if (errorBody.message == "이미 있는 닉네임입니다"){
                            _stateSnsSignUp.value = Constant.ApiState.ERROR
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

    fun checkDupEmail(email: String){
        checkDupApi.checkDupEmail(email).enqueue(object: Callback<ResultState>{
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        _stateDupEmail.value = body?.state
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