package com.infinity.omos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository
import com.kakao.sdk.user.UserApiClient


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 1500 // (ms)초 간 Splash 화면 띄움
    private val repository = OnBoardingRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // x초 뒤 Activity 이동
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val token = OmosApplication.prefs.getUserToken()
            if (token != null){
                repository.getUserToken(token)
            } else{
                moveActivity(this)
            }
        }, SPLASH_TIME)

        repository.stateToken.observe(this) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }

                    Constant.ApiState.DONE -> {
                        moveActivity(this)
                    }

                    else -> {
                        OmosApplication.prefs.setUserToken(null, null, -1)
                        moveActivity(this)
                    }
                }
            }
        }
    }

    private fun moveActivity(activity: Activity) {
        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 이메일 로그인 상태 확인
                if (OmosApplication.prefs.getInt("userId") == -1) {
                    Log.d("SplashActivity", "토큰 불러오기 실패")
                    val intent = Intent(activity, OnboardingActivity::class.java)
                    activity.startActivity(intent)
                } else {
                    Log.d("SplashActivity", "토큰 불러오기 성공")
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                }
            } else if (tokenInfo != null) {
                // 소셜 로그인 상태 확인
                if (OmosApplication.prefs.getInt("userId") == -1) {
                    // 소셜 로그인하고 닉네임 입력 안한 상황
                    Log.d("SplashActivity", "토큰 불러오기 실패")

                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.d("SplashActivity", "카카오 에러")
                        } else {
                            Log.d("SplashActivity", "카카오 로그아웃")
                        }
                    }

                    val intent = Intent(activity, OnboardingActivity::class.java)
                    activity.startActivity(intent)
                } else {
                    Log.d("SplashActivity", "토큰 불러오기 성공")
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                }
            }
            activity.finish()
        }
    }
}