package com.infinity.omos.ui.setting

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityChangePwBinding
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.ChangePwViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class ChangePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePwBinding
    private val viewModel: ChangePwViewModel by viewModels()
    private val userId = GlobalApplication.prefs.getInt("userId")

    private val pwPattern =
        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$" // 영문, 숫자, 특수문자 하나씩 포함, 8~16자

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pw)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        binding.etPw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (Pattern.matches(pwPattern, binding.etPw.text.toString())){
                    LoginActivity.hideErrorMsg(this, binding.etPw, binding.tvErrorPw)
                } else{
                    LoginActivity.showErrorMsg(
                        this,
                        binding.etPw,
                        binding.tvErrorPw,
                        resources.getString(R.string.condition_password),
                        binding.linearPw
                    )
                }
            }
        }

        binding.etPwAgain.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.text.toString() != binding.etPwAgain.text.toString()){
                    LoginActivity.showErrorMsg(
                        this,
                        binding.etPwAgain,
                        binding.tvErrorAgainPw,
                        resources.getString(R.string.no_match_password),
                        binding.linearPwAgain
                    )
                } else{
                    LoginActivity.hideErrorMsg(this, binding.etPwAgain, binding.tvErrorAgainPw)
                }
            }
        }

        binding.btnComplete.setOnClickListener {
            if (!Pattern.matches(pwPattern, binding.etPw.text.toString())){
                LoginActivity.showErrorMsg(
                    this,
                    binding.etPw,
                    binding.tvErrorPw,
                    resources.getString(R.string.condition_password),
                    binding.linearPw
                )
            } else if (binding.etPw.text.toString() != binding.etPwAgain.text.toString()){
                LoginActivity.showErrorMsg(
                    this,
                    binding.etPwAgain,
                    binding.tvErrorAgainPw,
                    resources.getString(R.string.no_match_password),
                    binding.linearPwAgain
                )
            } else{
                viewModel.updatePassword(binding.etPw.text.toString(), userId)
            }
        }

        viewModel.getStateUpdatePw().observe(this) { state ->
            state?.let {
                if(it.state){
                    finish()

                    Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "에러", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye1.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.icEye1.setImageResource(R.drawable.ic_visible_eye)
                    binding.etPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.etPw.setSelection(binding.etPw.length())
                } else{
                    binding.icEye1.setImageResource(R.drawable.ic_invisible_eye)
                    binding.etPw.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.etPw.setSelection(binding.etPw.length())
                }
            }
        })

        // 비밀번호 표시 ON/OFF
        viewModel.visibleEye2.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.icEye2.setImageResource(R.drawable.ic_visible_eye)
                    binding.etPwAgain.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.etPwAgain.setSelection(binding.etPwAgain.length())
                } else{
                    binding.icEye2.setImageResource(R.drawable.ic_invisible_eye)
                    binding.etPwAgain.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.etPwAgain.setSelection(binding.etPwAgain.length())
                }
            }
        })

        // 완료 버튼 활성화
        viewModel.stateInput.observe(this, Observer { state ->
            state?.let {
                if (it){
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                    binding.btnComplete.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnComplete.isEnabled = true
                } else{
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                    binding.btnComplete.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    binding.btnComplete.isEnabled = false
                }
            }
        })
    }

    private fun initToolBar(){
        toolbar.title = "비밀번호 변경"
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