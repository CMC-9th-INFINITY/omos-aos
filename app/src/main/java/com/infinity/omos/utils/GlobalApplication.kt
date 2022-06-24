package com.infinity.omos.utils

import android.app.Application
import com.infinity.omos.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

/**
 *  가장 먼저 실행
 */

@HiltAndroidApp
class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    companion object{
        lateinit var prefs: PreferenceUtil
    }
}