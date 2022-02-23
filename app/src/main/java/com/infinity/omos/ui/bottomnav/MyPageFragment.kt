package com.infinity.omos.ui.bottomnav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.infinity.omos.R
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.GlobalApplication
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null){
                    Log.d("MyPage", "이메일 로그아웃")
                } else{
                    Log.d("MyPage", "카카오 로그아웃")
                }

                Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                GlobalApplication.prefs.setUserToken(null, null, -1L)
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}