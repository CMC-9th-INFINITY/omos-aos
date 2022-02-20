package com.infinity.omos

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.databinding.ActivityRegisterBinding
import com.infinity.omos.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_id
import kotlinx.android.synthetic.main.activity_register.et_pw
import kotlinx.android.synthetic.main.activity_register.linear_id
import kotlinx.android.synthetic.main.activity_register.linear_pw
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.activity_register.tv_error_pw

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private var idState = false

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

        // 이메일 중복 확인
        viewModel.stateDupEmail.observe(this, Observer { state ->
            state?.let {
                if (!state){
                    LoginActivity.showErrorMsg(et_id, tv_success_msg, "이미 가입된 메일입니다.", linear_id)
                } else{
                    idState = true
                    tv_success_msg.text = "인증을 성공했어요."
                    tv_success_msg.setTextColor(ContextCompat.getColor(this, R.color.gray_05))
                    tv_success_msg.visibility = View.VISIBLE
                    btn_send_auth_mail.visibility = View.GONE
                    et_id.isEnabled = false
                }
            }
        })

        // 로그인 버튼 활성화
        viewModel.stateInput.observe(this, Observer { state ->
            state?.let {
                if (it){
                    btn_next.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                    btn_next.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_next.isEnabled = true
                } else{
                    btn_next.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray))
                    btn_next.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_next.isEnabled = false
                }
            }
        })

        et_id.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_id.length() == 0){
                    LoginActivity.showErrorMsg(et_id, tv_success_msg, "이메일을 입력해주세요.", linear_id)
                } else if (!et_id.text.contains("@")){
                    LoginActivity.showErrorMsg(et_id, tv_success_msg, "입력하신 내용을 다시 확인해주세요.", linear_id)
                } else{
                    if (!idState){
                        LoginActivity.hideErrorMsg(et_id, tv_success_msg)
                    }
                }
            }
        }

        et_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.length() < 8 || et_pw.length() > 16){
                    LoginActivity.showErrorMsg(et_pw, tv_error_pw, "8~16자의 영문 대소문자, 숫자, 특수문자만 가능해요.", linear_pw)
                } else{
                    LoginActivity.hideErrorMsg(et_pw, tv_error_pw)
                }
            }
        }

        et_again_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.text != et_again_pw.text){
                    LoginActivity.showErrorMsg(et_again_pw, tv_error_again_pw, "비밀번호가 일치하지 않아요.", linear_again_pw)
                } else{
                    LoginActivity.hideErrorMsg(et_again_pw, tv_error_again_pw)
                }
            }
        }

        // 닉네임 설정 페이지 이동
        btn_next.setOnClickListener {
            if (!idState){
                LoginActivity.showErrorMsg(et_id, tv_success_msg, "입력하신 내용을 다시 확인해주세요.", linear_id)
            } else if (et_pw.length() < 8 || et_pw.length() > 16){
                LoginActivity.showErrorMsg(et_pw, tv_error_pw, "8~16자의 영문 대소문자, 숫자, 특수문자만 가능해요.", linear_pw)
            }else if (et_pw.text != et_again_pw.text){
                LoginActivity.showErrorMsg(et_again_pw, tv_error_again_pw, "비밀번호가 일치하지 않아요.", linear_again_pw)
            } else{
                val intent = Intent(this, RegisterNickActivity::class.java)
                startActivity(intent)
            }
        }

        // 인증메일 전송
        btn_send_auth_mail.setOnClickListener {
            if (et_id.length() == 0){
                LoginActivity.showErrorMsg(et_id, tv_success_msg, "이메일을 입력해주세요.", linear_id)
            } else if (!et_id.text.contains("@")){
                LoginActivity.showErrorMsg(et_id, tv_success_msg, "입력하신 내용을 다시 확인해주세요.", linear_id)
            } else{
                LoginActivity.hideErrorMsg(et_id, tv_success_msg)
                viewModel.checkDupEmail(et_id.text.toString())
            }
        }
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}