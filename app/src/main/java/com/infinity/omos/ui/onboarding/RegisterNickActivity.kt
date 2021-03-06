package com.infinity.omos.ui.onboarding

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.data.UserSignUp
import com.infinity.omos.data.UserSnsSignUp
import com.infinity.omos.databinding.ActivityRegisterNickBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.GlobalFunction.Companion.changeTextColor
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.viewmodels.RegisterNickViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class RegisterNickActivity : AppCompatActivity() {

    private val viewModel: RegisterNickViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterNickBinding>(this,
            R.layout.activity_register_nick
        )
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var isSNS = intent.getBooleanExtra("sns", false)
        var userId = intent.getStringExtra("userId")
        var userPw = intent.getStringExtra("userPw")

        initToolBar()

        // 텍스트 색상 변경
        changeTextColor(this, tv_tos, 4, 9, R.color.orange)
        changeTextColor(this, tv_pp, 4, 14, R.color.orange)

        // 이용약관 체크박스 클릭 시
        viewModel.checkBoxTos.observe(this, Observer { state ->
            state?.let {
                if (it){
                    checkbox_tos.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                } else{
                    checkbox_tos.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                }
            }
        })

        // 개인정보 보호정책 체크박스 클릭 시
        viewModel.checkBoxPP.observe(this, Observer { state ->
            state?.let {
                if (it){
                    checkbox_pp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                } else{
                    checkbox_pp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                }
            }
        })

        // 완료 버튼 활성화
        viewModel.allState.observe(this, Observer { state ->
            state?.let {
                if (it && et_nick.length() > 0){
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                    btn_complete.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_complete.isEnabled = true
                } else{
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                    btn_complete.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_complete.isEnabled = false
                }
            }
        })

        et_nick.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (viewModel.allState.value == true && et_nick.length() > 0){
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,
                        R.color.orange
                    ))
                    btn_complete.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                    btn_complete.isEnabled = true
                } else{
                    btn_complete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext,
                        R.color.light_gray
                    ))
                    btn_complete.setTextColor(ContextCompat.getColor(applicationContext, R.color.dark_gray))
                    btn_complete.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        // 회원가입 완료
        viewModel.getStateSignUp().observe(this, Observer { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.DONE -> {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                    Constant.ApiState.ERROR -> {
                        // 이미 있는 닉네임일 때,
                        LoginActivity.showErrorMsg(
                            this,
                            et_nick,
                            tv_error_nick,
                            resources.getString(R.string.exist_nick),
                            linear_nick
                        )
                    }
                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, "회원가입 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        // 회원가입 완료
        viewModel.getStateSnsSignUp().observe(this, Observer { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.DONE -> {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    Constant.ApiState.ERROR -> {
                        // 이미 있는 닉네임일 때,
                        LoginActivity.showErrorMsg(
                            this,
                            et_nick,
                            tv_error_nick,
                            resources.getString(R.string.exist_nick),
                            linear_nick
                        )
                    }
                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "네트워크 상태가 불안정합니다.", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, "회원가입 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        // 특수문자 띄어쓰기 입력 제한
        binding.etNick.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    Toast.makeText(this, "특수문자 및 공백은 입력이 제한됩니다.", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        // 회원가입 완료
        btn_complete.setOnClickListener {
            if (isSNS){
                // 소셜 회원가입
                viewModel.snsSignUp(UserSnsSignUp(userId!!, et_nick.text.toString()))
            } else{
                viewModel.signUp(UserSignUp(userId!!, et_nick.text.toString(), userPw!!))
            }
        }

        // 이용약관 보기
        btn_look_tos.setOnClickListener {
            val dialog = CustomDialog(this)
            dialog.showTosDialog()
        }

        // 개인정보 보호정책 보기
        btn_look_pp.setOnClickListener {
            val dialog = CustomDialog(this)
            dialog.showPPDialog()
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