package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

/**
 * onboarding Navigation component 적용을 위한 Activity
 * 추후 MainActivity로 변경 예정
 */
@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}