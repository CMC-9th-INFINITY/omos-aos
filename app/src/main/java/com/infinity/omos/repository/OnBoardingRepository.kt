package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.FakeAuthService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.di.OmosApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class OnBoardingRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingApi = retrofit.create(FakeAuthService::class.java)

    var getUserId = MutableLiveData<UserId>()
    fun getUserIdFromEmail(email: String){
        onBoardingApi.getUserIdFromEmail(email).enqueue(object: Callback<UserId> {
            override fun onResponse(
                call: Call<UserId>,
                response: Response<UserId>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("UserIdAPI", "Success")
                        getUserId.postValue(body!!)
                    }

                    401 -> {
                        Log.d("UserIdAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("UserIdAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("UserIdAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<UserId>, t: Throwable) {
                Log.d("UserIdAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var stateUpdatePw = MutableLiveData<ResultState>()
    fun updatePassword(password: String, userId: Int){
        onBoardingApi.updatePassword(Password(password, userId)).enqueue(object: Callback<ResultState> {
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("UpdatePwAPI", "Success")
                        stateUpdatePw.postValue(body!!)
                    }

                    401 -> {
                        Log.d("UpdatePwAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("UpdatePwAPI", errorBody!!.message)
                    }

                    else -> {
                        Log.d("UpdatePwAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("UpdatePwAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

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
                stateGetCode.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }

    var stateToken = MutableLiveData<Constant.ApiState>()
    fun getUserToken(userInfo: FakeUserToken){
        onBoardingApi.getToken(userInfo).enqueue(object: Callback<FakeUserToken> {
            override fun onResponse(
                call: Call<FakeUserToken>,
                response: Response<FakeUserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ReissueAPI", "Save Token")
                        OmosApplication.prefs.setUserToken(
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

            override fun onFailure(call: Call<FakeUserToken>, t: Throwable) {
                Log.d("ReissueAPI fail", t.message.toString())
                stateToken.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }

    var stateLogin = MutableLiveData<Constant.ApiState>()
    fun checkLogin(userLogin: UserLogin){
        onBoardingApi.getResultLogin(userLogin).enqueue(object: Callback<FakeUserToken> {
            override fun onResponse(
                call: Call<FakeUserToken>,
                response: Response<FakeUserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("LoginAPI", "Success Login")
                        OmosApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)

                        stateLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        // 아이디만 맞고 비밀번호는 틀릴 경우
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

            override fun onFailure(call: Call<FakeUserToken>, t: Throwable) {
                Log.d("LoginAPI", t.message.toString())
                stateLogin.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }

    var stateSnsLogin = MutableLiveData<Constant.ApiState>()
    fun checkSnsLogin(id: UserSnsLogin){
        onBoardingApi.getResultSnsLogin(id).enqueue(object: Callback<FakeUserToken> {
            override fun onResponse(
                call: Call<FakeUserToken>,
                response: Response<FakeUserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SnsLoginAPI", "Success Login")
                        OmosApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)

                        stateSnsLogin.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("SnsLoginAPI", "Unauthorized")
                        stateSnsLogin.value = Constant.ApiState.ERROR
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SnsLoginAPI", errorBody!!.message)
                        stateSnsLogin.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("SnsLoginAPI", "Code: $code")
                        stateSnsLogin.value = Constant.ApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<FakeUserToken>, t: Throwable) {
                Log.d("SnsLoginAPI", t.message.toString())
                stateSnsLogin.value = Constant.ApiState.NETWORK
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
                        stateSignUp.value = Constant.ApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                stateSignUp.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }

    var stateSnsSignUp = MutableLiveData<Constant.ApiState>()
    fun snsSignUp(userInfo: UserSnsSignUp){
        onBoardingApi.getResultSnsSignUp(userInfo).enqueue(object: Callback<FakeUserToken>{
            override fun onResponse(
                call: Call<FakeUserToken>,
                response: Response<FakeUserToken>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("signUpAPI", "Success SignUp")
                        OmosApplication.prefs.setUserToken(
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
                        stateSnsSignUp.value = Constant.ApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<FakeUserToken>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                stateSnsSignUp.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }

    var stateDupEmail = MutableLiveData<Constant.ApiState>()
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
                        if (body?.state == true){
                            stateDupEmail.value = Constant.ApiState.DONE
                        } else{
                            stateDupEmail.value = Constant.ApiState.ERROR
                        }
                    }

                    401 -> {
                        Log.d("checkDupEmailAPI", "Unauthorized")
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("checkDupEmailAPI", errorBody!!.message)
                        stateDupEmail.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("checkDupEmailAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("checkDupEmailAPI", t.message.toString())
                stateDupEmail.value = Constant.ApiState.NETWORK
                t.stackTrace
            }
        })
    }
}