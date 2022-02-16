package com.infinity.omos.utils

import android.app.Application
import com.infinity.omos.BuildConfig
import com.kakao.sdk.common.KakaoSdk

/**
 *  가장 먼저 실행
 */
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