package com.infinity.omos

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.databinding.ActivityRegisterBinding
import com.infinity.omos.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.et_pw
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye1.observe(this, Observer { state ->
            state?.let {
                if (it){
                    ic_eye1.setImageResource(R.drawable.ic_visible_eye)
                    et_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                } else{
                    ic_eye1.setImageResource(R.drawable.ic_invisible_eye)
                    et_pw.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                }
            }
        })

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye2.observe(this, Observer { state ->
            state?.let {
                if (it){
                    ic_eye2.setImageResource(R.drawable.ic_visible_eye)
                    et_again_pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                } else{
                    ic_eye2.setImageResource(R.drawable.ic_invisible_eye)
                    et_again_pw.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_pw.setSelection(et_pw.length())
                }
            }
        })

        // 닉네임 설정 페이지 이동
        btn_next.setOnClickListener {
            var intent = Intent(this, RegisterNickActivity::class.java)
            startActivity(intent)
        }

        // 인증메일 전송
        btn_send_auth_mail.setOnClickListener {
            tv_success_msg.visibility = View.VISIBLE
            btn_send_auth_mail.visibility = View.GONE
        }
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}