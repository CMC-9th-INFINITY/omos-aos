package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.api.TodayService
import com.infinity.omos.data.LovedMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Profile
import com.infinity.omos.data.SumRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TodayRepository {
    private val retrofit: Retrofit = RetrofitAPI.getInstnace()

    private val todayApi = retrofit.create(TodayService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var famousRecord = MutableLiveData<List<SumRecord>>()
    var stateFamousRecord = MutableLiveData<Constant.ApiState>()
    fun getFamousRecord(){
        stateFamousRecord.value = Constant.ApiState.LOADING
        todayApi.getFamousRecord().enqueue(object:
            Callback<List<SumRecord>> {
            override fun onResponse(
                call: Call<List<SumRecord>>,
                response: Response<List<SumRecord>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("FamousRecordAPI", "Success")
                        famousRecord.postValue(body!!)
                        stateFamousRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("FamousRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getFamousRecord()
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("FamousRecordAPI", errorBody!!.message)
                        stateFamousRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("FamousRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<SumRecord>>, t: Throwable) {
                Log.d("FamousRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var loveMusic = MutableLiveData<LovedMusic>()
    var stateLoveMusic = MutableLiveData<Constant.ApiState>()
    fun getMyLoveMusic(userId: Int){
        stateLoveMusic.value = Constant.ApiState.LOADING
        todayApi.getMyLoveMusic(userId).enqueue(object:
            Callback<LovedMusic> {
            override fun onResponse(
                call: Call<LovedMusic>,
                response: Response<LovedMusic>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MyLoveMusicAPI", "Success")
                        loveMusic.postValue(body!!)
                        stateLoveMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MyLoveMusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMyLoveMusic(userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MyLoveMusicAPI", errorBody!!.message)
                        stateLoveMusic.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MyLoveMusicAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<LovedMusic>, t: Throwable) {
                Log.d("MyLoveMusicAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var todayMusic = MutableLiveData<Music>()
    var stateTodayMusic = MutableLiveData<Constant.ApiState>()
    fun getTodayMusic(){
        stateTodayMusic.value = Constant.ApiState.LOADING
        todayApi.getTodayMusic().enqueue(object:
            Callback<Music> {
            override fun onResponse(
                call: Call<Music>,
                response: Response<Music>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("TodayMusicAPI", "Success")
                        todayMusic.postValue(body!!)
                        stateTodayMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("TodayMusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getTodayMusic()
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("TodayMusicAPI", errorBody!!.message)
                        stateTodayMusic.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("TodayMusicAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<Music>, t: Throwable) {
                Log.d("TodayMusicAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var recommendDj = MutableLiveData<List<Profile>>()
    var stateRecommendDj = MutableLiveData<Constant.ApiState>()
    fun getRecommendDj(){
        stateRecommendDj.value = Constant.ApiState.LOADING
        todayApi.getRecommendDj().enqueue(object:
            Callback<List<Profile>> {
            override fun onResponse(
                call: Call<List<Profile>>,
                response: Response<List<Profile>>
            ) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("RecommendDjAPI", "Success")
                        recommendDj.postValue(body!!)
                        stateRecommendDj.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("RecommendDjAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getRecommendDj()
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("RecommendDjAPI", errorBody!!.message)
                        stateRecommendDj.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("RecommendDjAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
                Log.d("RecommendDjAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}