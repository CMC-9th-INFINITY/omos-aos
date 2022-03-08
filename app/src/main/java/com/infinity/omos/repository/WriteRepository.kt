package com.infinity.omos.repository

import com.infinity.omos.api.RetrofitAPI
import retrofit2.Retrofit

class WriteRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()


}