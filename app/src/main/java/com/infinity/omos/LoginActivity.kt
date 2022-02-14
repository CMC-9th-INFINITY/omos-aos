package com.infinity.omos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.InputType.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.databinding.ActivityLoginBinding
import com.infinity.omos.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye.observe(this, Observer { state ->
            state?.let {
                if (it){
                    ic_eye.setImageResource(R.drawable.ic_visible_eye)
                    et_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                } else{
                    ic_eye.setImageResource(R.drawable.ic_invisible_eye)
                    et_pw.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                }
            }
        })

        // 로그인 완료
        btn_login.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 소셜 로그인 페이지 이동
        btn_kakao_login.setOnClickListener {
            Toast.makeText(this, resources.getString(R.string._ing), Toast.LENGTH_SHORT).show()
        }

        // 회원가입 페이지 이동
        btn_register.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 아이디 찾기 페이지 이동
        btn_find_id.setOnClickListener {
            Toast.makeText(this, resources.getString(R.string._ing), Toast.LENGTH_SHORT).show()
        }

        // 비밀번호 찾기 페이지 이동
        btn_find_pw.setOnClickListener {
            Toast.makeText(this, resources.getString(R.string._ing), Toast.LENGTH_SHORT).show()
        }
    }
}