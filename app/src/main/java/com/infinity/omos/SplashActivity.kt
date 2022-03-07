package com.infinity.omos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.ui.onboarding.RegisterNickActivity
import com.infinity.omos.utils.GlobalApplication
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000 // (ms)초 간 Splash 화면 띄움
    private val repository = OnBoardingRepository()
    private var stateToken = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 토큰 정보 확인
        val token = GlobalApplication.prefs.getUserToken()
        if (token != null){
            repository.getUserToken(token!!)
        }

        repository._stateToken.observe(this, Observer { state ->
            state?.let {
                stateToken = it != Constant.ApiState.ERROR
            }
        })

        // x초 뒤 Activity 이동
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // 로그인 정보 확인
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    // 이메일 로그인 상태 확인
                    if (GlobalApplication.prefs.getLong("userId") == -1L && !stateToken){
                        Log.d("SplashActivity", "토큰 불러오기 실패")
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else{
                        Log.d("SplashActivity", "토큰 불러오기 성공")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                else if (tokenInfo != null) {
                    // 소셜 로그인 상태 확인
                    if (GlobalApplication.prefs.getLong("userId") == -1L && !stateToken){
                        // 소셜 로그인하고 닉네임 입력 안한 상황
                        Log.d("SplashActivity", "토큰 불러오기 실패")
                        val intent = Intent(this, RegisterNickActivity::class.java)
                        intent.putExtra("sns", true)
                        intent.putExtra("userId", tokenInfo.id.toString()+"@kakao.com")
                        startActivity(intent)
                    } else{
                        Log.d("SplashActivity", "토큰 불러오기 성공")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                finish()
            }
        }, SPLASH_TIME)
    }
}