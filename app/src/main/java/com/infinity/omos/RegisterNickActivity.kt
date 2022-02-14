package com.infinity.omos

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.databinding.ActivityRegisterBinding
import com.infinity.omos.databinding.ActivityRegisterNickBinding
import com.infinity.omos.viewmodels.RegisterNickViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_nick.*

class RegisterNickActivity : AppCompatActivity() {

    private val viewModel: RegisterNickViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterNickBinding>(this, R.layout.activity_register_nick)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 이용약관 체크박스 클릭 시
        viewModel.checkBoxTos.observe(this, Observer { state ->
            state?.let {
                if (it){
                    checkbox_tos.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                } else{
                    checkbox_tos.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray))
                }
            }
        })

        // 개인정보 보호정책 체크박스 클릭 시
        viewModel.checkBoxPP.observe(this, Observer { state ->
            state?.let {
                if (it){
                    checkbox_pp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                } else{
                    checkbox_pp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray))
                }
            }
        })

        // 완료 버튼 활성화
        viewModel.allState.observe(this, Observer { state ->
            state?.let {
                if (it){
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                    btn_complete.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_complete.isEnabled = true
                } else{
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray))
                    btn_complete.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_complete.isEnabled = false
                }
            }
        })

        // 로그인 페이지 이동
        btn_complete.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // 이용약관 보기
        btn_look_tos.setOnClickListener {
            Toast.makeText(this, resources.getString(R.string._ing), Toast.LENGTH_SHORT).show()
        }

        // 개인정보 보호정책 보기
        btn_look_pp.setOnClickListener {
            Toast.makeText(this, resources.getString(R.string._ing), Toast.LENGTH_SHORT).show()
        }
    }
}