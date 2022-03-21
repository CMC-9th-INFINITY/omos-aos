package com.infinity.omos.ui.setting

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityChangeProfileBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.ui.bottomnav.MyPageFragment
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.ChangeProfileViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.activity_register_nick.*
import android.text.Spanned

import android.text.InputFilter




class ChangeProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeProfileBinding
    private val viewModel: ChangeProfileViewModel by viewModels()

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_profile)
        binding.lifecycleOwner = this
        binding.etNick.setText(MyPageFragment.myNickName)

        initToolBar()

        binding.btnComplete.setOnClickListener {
            if (binding.etNick.text.toString() == MyPageFragment.myNickName){
                // 기존과 동일한 닉네임일 때,
                LoginActivity.showErrorMsg(
                    this,
                    binding.etNick,
                    binding.tvErrorNick,
                    "기존과 동일한 닉네임입니다.",
                    binding.linearNick
                )
            } else{
                viewModel.updateProfile(binding.etNick.text.toString(), "", userId)
            }
        }

        viewModel.getStateUpdateProfile().observe(this) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.DONE -> {
                        val intent = Intent("PROFILE_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                        finish()

                        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                    }
                    Constant.ApiState.ERROR -> {
                        // 이미 있는 닉네임일 때,
                        LoginActivity.showErrorMsg(
                            this,
                            binding.etNick,
                            binding.tvErrorNick,
                            resources.getString(R.string.exist_nick),
                            binding.linearNick
                        )
                    }
                    else -> {
                        Toast.makeText(this, "에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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

        // 완료버튼 활성화
        binding.etNick.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etNick.length() > 0){
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@ChangeProfileActivity,
                        R.color.orange
                    ))
                    binding.btnComplete.setTextColor(ContextCompat.getColor(this@ChangeProfileActivity, R.color.white))
                    binding.btnComplete.isEnabled = true
                } else{
                    binding.btnComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@ChangeProfileActivity,
                            R.color.light_gray
                        ))
                    binding.btnComplete.setTextColor(ContextCompat.getColor(this@ChangeProfileActivity, R.color.dark_gray))
                    binding.btnComplete.isEnabled = false
                }
            }
        })
    }

    private fun initToolBar(){
        toolbar.title = "프로필 변경"
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