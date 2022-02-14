package com.infinity.omos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000 // (ms)초 간 Splash 화면 띄움

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // x초 뒤 Activity 이동
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // 로그인 정보 확인
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("LoginActivity", "토큰 불러오기 실패")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if (tokenInfo != null) {
                    Log.d("LoginActivity", "토큰 불러오기 성공")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, SPLASH_TIME)
    }
}