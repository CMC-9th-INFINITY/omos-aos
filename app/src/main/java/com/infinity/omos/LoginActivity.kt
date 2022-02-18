package com.infinity.omos

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.infinity.omos.databinding.ActivityLoginBinding
import com.infinity.omos.viewmodels.LoginViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this

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

        // 로그인 버튼 활성화
        viewModel.stateInput.observe(this, Observer { state ->
            state?.let {
                if (it){
                    btn_login.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                    btn_login.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_login.isEnabled = true
                } else{
                    btn_login.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray))
                    btn_login.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_login.isEnabled = false
                }
            }
        })

        // 소셜 로그인 콜백
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {

                // 토큰, 사용자 정보 viewModel에 저장
                UserApiClient.instance.me { user, error ->
                    viewModel.addKakaoUser(token.accessToken, user!!.id)
                }

                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Id 입력란 포커스 잃었을 때,
        et_id.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_id.length() == 0){
                    showErrorMsg(et_id, tv_error_id, "이메일을 입력해주세요.", linear_id)
                } else if (!et_id.text.contains("@")){
                    showErrorMsg(et_id, tv_error_id, "입력하신 내용을 다시 확인해주세요.", linear_id)
                } else{
                    hideErrorMsg(et_id, tv_error_id)
                }
            }
        }

        // Pw 입력란 포커스 잃었을 때,
        et_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.length() == 0){
                    showErrorMsg(et_pw, tv_error_pw, "비밀번호를 입력해주세요.", linear_pw)
                } else{
                    hideErrorMsg(et_pw, tv_error_pw)
                }
            }
        }

        // 로그인 완료
        btn_login.setOnClickListener {
            if (viewModel.checkLogin(et_id.text.toString(), et_pw.text.toString())){
                var intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else{
                showErrorMsg(et_id, tv_error_id, "입력하신 내용을 다시 확인해주세요.", linear_id)
            }
        }

        // 카카오 소셜 로그인 페이지 이동
        btn_kakao_login.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
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

    companion object{
        lateinit var context: Context

        fun showErrorMsg(et: EditText, tvMsg: TextView, msg: String, shakeLayout: LinearLayout){
            et.background = ResourcesCompat.getDrawable(context.resources, R.drawable.rectangle_stroke_box, null)
            tvMsg.visibility = View.VISIBLE
            tvMsg.text = msg

            // 흔들림 효과
            YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(shakeLayout)
        }

        fun hideErrorMsg(et: EditText, tvMsg: TextView){
            et.background = ResourcesCompat.getDrawable(context.resources, R.drawable.rectangle_box, null)
            tvMsg.visibility = View.INVISIBLE
        }
    }
}