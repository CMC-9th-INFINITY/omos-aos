package com.infinity.omos.ui.onboarding

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityFindPwBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.ui.setting.ChangePwActivity
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.viewmodels.FindPwViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class FindPwActivity : AppCompatActivity() {

    private val viewModel: FindPwViewModel by viewModels()
    private var emailCode = ""
    private var idState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()

        val binding = DataBindingUtil.setContentView<ActivityFindPwBinding>(this,
            R.layout.activity_find_pw
        )
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 인증코드 표시 ON/OFF
        viewModel.visibleEye.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.icEye.setImageResource(R.drawable.ic_visible_eye)
                    binding.etCode.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.etCode.setSelection(binding.etCode.length())
                } else{
                    binding.icEye.setImageResource(R.drawable.ic_invisible_eye)
                    binding.etCode.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.etCode.setSelection(binding.etCode.length())
                }
            }
        })

        // 이메일 인증코드
        viewModel.getCode().observe(this) { code ->
            code?.let {
                binding.linearAuthMail.visibility = View.VISIBLE
                emailCode = it

                binding.btnSendAuthMail.text = resources.getString(R.string.send_again_auth_mail)
                binding.btnSendAuthMail.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                binding.btnSendAuthMail.isEnabled = true
            }
        }
        viewModel.getStateGetCode().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        Log.d("FindPwActivity", "이메일 인증 코드 오류")
                    }
                }
            }
        }

        // 이메일 중복 확인
        viewModel.getStateDupEmail().observe(this) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.DONE -> {
                        LoginActivity.showErrorMsg(
                            this,
                            binding.etId,
                            binding.tvSuccessMsg,
                            resources.getString(R.string.no_exist_email),
                            binding.linearId
                        )
                    }

                    Constant.ApiState.ERROR -> {
                        binding.btnSendAuthMail.text = "전송 중..."
                        binding.btnSendAuthMail.paintFlags = 0
                        binding.btnSendAuthMail.isEnabled = false
                        LoginActivity.hideErrorMsg(this, binding.etId, binding.tvSuccessMsg)
                        viewModel.sendEmailAuth(binding.etId.text.toString())
                    }

                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        Log.d("FindPwActivity", "이메일 중복 오류")
                    }
                }
            }
        }

        // 로그인 버튼 활성화
        viewModel.stateInput.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this,
                        R.color.orange
                    ))
                    binding.btnNext.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnNext.isEnabled = true
                } else{
                    binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                    binding.btnNext.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    binding.btnNext.isEnabled = false
                }
            }
        })

        binding.etId.setOnFocusChangeListener { _, b ->
            if (!b){
                if (binding.etId.length() == 0){
                    LoginActivity.showErrorMsg(
                        this,
                        binding.etId,
                        binding.tvSuccessMsg,
                        resources.getString(R.string.error_input_email),
                        binding.linearId
                    )
                } else if (!binding.etId.text.contains("@")){
                    LoginActivity.showErrorMsg(
                        this,
                        binding.etId,
                        binding.tvSuccessMsg,
                        resources.getString(R.string.again_check),
                        binding.linearId
                    )
                } else{
                    if (!idState){
                        LoginActivity.hideErrorMsg(this, binding.etId, binding.tvSuccessMsg)
                    }
                }
            }
        }

        binding.etCode.setOnFocusChangeListener { view, b ->
            if (!b){
                if (emailCode == binding.etCode.text.toString()){
                    idState = true
                    binding.tvSuccessMsg.text = resources.getString(R.string.success_auth)
                    binding.tvSuccessMsg.setTextColor(ContextCompat.getColor(this, R.color.gray_05))
                    binding.tvSuccessMsg.visibility = View.VISIBLE
                    binding.btnSendAuthMail.visibility = View.GONE
                    binding.etId.isEnabled = false
                    binding.etCode.isEnabled = false
                    LoginActivity.hideErrorMsg(this, binding.etCode, binding.tvErrorAuthMail)
                } else{
                    LoginActivity.showErrorMsg(
                        this,
                        binding.etCode,
                        binding.tvErrorAuthMail,
                        resources.getString(R.string.no_match_code),
                        binding.linearAuthMail
                    )
                }
            }
        }

        // 닉네임 설정 페이지 이동
        binding.btnNext.setOnClickListener {
            if (emailCode == binding.etCode.text.toString()){
                idState = true
                binding.tvSuccessMsg.text = resources.getString(R.string.success_auth)
                binding.tvSuccessMsg.setTextColor(ContextCompat.getColor(this, R.color.gray_05))
                binding.tvSuccessMsg.visibility = View.VISIBLE
                binding.btnSendAuthMail.visibility = View.GONE
                binding.etId.isEnabled = false
                binding.etCode.isEnabled = false
                LoginActivity.hideErrorMsg(this, binding.etCode, binding.tvErrorAuthMail)

                val intent = Intent(this, ChangePwActivity::class.java)
                startActivity(intent)
            } else{
                LoginActivity.showErrorMsg(
                    this,
                    binding.etCode,
                    binding.tvErrorAuthMail,
                    resources.getString(R.string.no_match_code),
                    binding.linearAuthMail
                )
            }
        }

        // 인증메일 전송
        binding.btnSendAuthMail.setOnClickListener {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS

            if (binding.etId.length() == 0){
                LoginActivity.showErrorMsg(
                    this,
                    binding.etId,
                    binding.tvSuccessMsg,
                    resources.getString(R.string.error_input_email),
                    binding.linearId
                )
            } else if (!pattern.matcher(binding.etId.text).matches()){
                LoginActivity.showErrorMsg(
                    this,
                    binding.etId,
                    binding.tvSuccessMsg,
                    resources.getString(R.string.again_check),
                    binding.linearId
                )
            } else{
                val dlg = CustomDialog(this)
                dlg.show("인증메일을 전송하시겠습니까?", "전송")
                dlg.setOnOkClickedListener {
                    when(it){
                        "yes" -> {
                            viewModel.checkDupEmail(binding.etId.text.toString())
                        }
                    }
                }
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