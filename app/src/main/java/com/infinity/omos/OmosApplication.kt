package com.infinity.omos

import android.app.Application
import com.infinity.omos.utils.PreferenceUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class OmosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        Timber.plant(Timber.DebugTree())
    }

    companion object{
        lateinit var prefs: PreferenceUtil
    }
}