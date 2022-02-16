package com.infinity.omos

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.infinity.omos.databinding.ActivityRegisterNickBinding
import com.infinity.omos.viewmodels.RegisterNickViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_register_nick.toolbar

class RegisterNickActivity : AppCompatActivity() {

    private val viewModel: RegisterNickViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterNickBinding>(this, R.layout.activity_register_nick)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        // 텍스트 색상 변경
        changeTextColor(tv_tos, 4, 9, R.color.orange)
        changeTextColor(tv_pp, 4, 14, R.color.orange)

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

    private fun changeTextColor(tv: TextView, start: Int, end: Int, color: Int){
        var ssb = SpannableStringBuilder(tv.text)
        ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = ssb
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