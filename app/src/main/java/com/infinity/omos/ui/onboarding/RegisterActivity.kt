package com.infinity.omos.ui.onboarding

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityRegisterBinding
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private var idState = false
    private val pwPattern =
        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$" // 영문, 숫자, 특수문자 하나씩 포함, 8~16자
    private var emailCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this,
            R.layout.activity_register
        )
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
                    et_again_pw.setSelection(et_again_pw.length())
                } else{
                    ic_eye2.setImageResource(R.drawable.ic_invisible_eye)
                    et_again_pw.transformationMethod = PasswordTransformationMethod.getInstance()
                    et_again_pw.setSelection(et_again_pw.length())
                }
            }
        })

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye3.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.icEye3.setImageResource(R.drawable.ic_visible_eye)
                    binding.etCode.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.etCode.setSelection(binding.etCode.length())
                } else{
                    binding.icEye3.setImageResource(R.drawable.ic_invisible_eye)
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

        // 이메일 중복 확인
        viewModel.getStateDupEmail().observe(this, Observer { state ->
            state?.let {
                if (!state){
                    LoginActivity.showErrorMsg(
                        this,
                        et_id,
                        tv_success_msg,
                        resources.getString(R.string.exist_email),
                        linear_id
                    )
                } else{
                    viewModel.sendEmailAuth(binding.etId.text.toString())
                }
            }
        })

        // 로그인 버튼 활성화
        viewModel.stateInput.observe(this, Observer { state ->
            state?.let {
                if (it){
                    btn_next.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                    btn_next.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_next.isEnabled = true
                } else{
                    btn_next.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                    btn_next.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_next.isEnabled = false
                }
            }
        })

        et_id.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_id.length() == 0){
                    LoginActivity.showErrorMsg(
                        this,
                        et_id,
                        tv_success_msg,
                        resources.getString(R.string.error_input_email),
                        linear_id
                    )
                } else if (!et_id.text.contains("@")){
                    LoginActivity.showErrorMsg(
                        this,
                        et_id,
                        tv_success_msg,
                        resources.getString(R.string.again_check),
                        linear_id
                    )
                } else{
                    if (!idState){
                        LoginActivity.hideErrorMsg(this, et_id, tv_success_msg)
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

        et_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (Pattern.matches(pwPattern, et_pw.text.toString())){
                    LoginActivity.hideErrorMsg(this, et_pw, tv_error_pw)
                } else{
                    LoginActivity.showErrorMsg(
                        this,
                        et_pw,
                        tv_error_pw,
                        resources.getString(R.string.condition_password),
                        linear_pw
                    )
                }
            }
        }

        et_again_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.text.toString() != et_again_pw.text.toString()){
                    LoginActivity.showErrorMsg(
                        this,
                        et_again_pw,
                        tv_error_again_pw,
                        resources.getString(R.string.no_match_password),
                        linear_again_pw
                    )
                } else{
                    LoginActivity.hideErrorMsg(this, et_again_pw, tv_error_again_pw)
                }
            }
        }

        // 닉네임 설정 페이지 이동
        btn_next.setOnClickListener {
            if (!idState){
                LoginActivity.showErrorMsg(
                    this,
                    et_id,
                    tv_success_msg,
                    resources.getString(R.string.again_check),
                    linear_id
                )
            } else if (et_pw.length() < 8 || et_pw.length() > 16){
                LoginActivity.showErrorMsg(
                    this,
                    et_pw,
                    tv_error_pw,
                    resources.getString(R.string.condition_password),
                    linear_pw
                )
            }else if (et_pw.text.toString() != et_again_pw.text.toString()){
                LoginActivity.showErrorMsg(
                    this,
                    et_again_pw,
                    tv_error_again_pw,
                    resources.getString(R.string.no_match_password),
                    linear_again_pw
                )
            } else{
                val intent = Intent(this, RegisterNickActivity::class.java)
                intent.putExtra("sns", false)
                intent.putExtra("userId", et_id.text.toString())
                intent.putExtra("userPw", et_pw.text.toString())
                startActivity(intent)
            }
        }

        // 인증메일 전송
        binding.btnSendAuthMail.setOnClickListener {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS

            if (et_id.length() == 0){
                LoginActivity.showErrorMsg(
                    this,
                    et_id,
                    tv_success_msg,
                    resources.getString(R.string.error_input_email),
                    linear_id
                )
            } else if (!pattern.matcher(et_id.text).matches()){
                LoginActivity.showErrorMsg(
                    this,
                    et_id,
                    tv_success_msg,
                    resources.getString(R.string.again_check),
                    linear_id
                )
            } else{
                val dlg = CustomDialog(this)
                dlg.show("인증메일을 전송하시겠습니까?", "전송")
                dlg.setOnOkClickedListener {
                    when(it){
                        "yes" -> {
                            binding.btnSendAuthMail.text = "전송 중..."
                            binding.btnSendAuthMail.paintFlags = 0
                            binding.btnSendAuthMail.isEnabled = false
                            LoginActivity.hideErrorMsg(this, et_id, tv_success_msg)
                            viewModel.checkDupEmail(et_id.text.toString())
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