package com.infinity.omos.ui.onboarding

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
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
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.data.UserLogin
import com.infinity.omos.data.UserSnsLogin
import com.infinity.omos.databinding.ActivityLoginBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.viewmodels.LoginViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private var userId = ""
    private lateinit var dlg: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        dlg = CustomDialog(this)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,
            R.layout.activity_login
        )
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
                    btn_login.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.orange
                    ))
                    btn_login.setTextColor(ContextCompat.getColor(this, R.color.white))
                    btn_login.isEnabled = true
                } else{
                    btn_login.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.light_gray
                    ))
                    btn_login.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
                    btn_login.isEnabled = false
                }
            }
        })

        // 이미 가입된 회원인지 확인
        viewModel.getStateSnsLogin().observe(this, Observer { state ->
            state?.let {
                if (it == Constant.ApiState.DONE){
                    Log.d("LoginActivity", "이미 가입된 회원입니다.")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else{
                    Log.d("LoginActivity", "회원가입이 필요합니다.")
                    val intent = Intent(this, RegisterNickActivity::class.java)
                    intent.putExtra("sns", true)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
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
                        Log.d("LoginActivity", "기타 에러")
                    }
                }
            }
            else if (token != null) {

                // 소셜 로그인 인증 성공 시 이미 가입된 회원인지 확인
                UserApiClient.instance.me { user, error ->
                    userId = user?.id.toString()+"@kakao.com"
                    viewModel.checkSnsLogin(UserSnsLogin(userId))
                }
            }
        }

        // Id 입력란 포커스 잃었을 때,
        et_id.setOnFocusChangeListener { view, b ->
            if (!b){
                val pattern: Pattern = Patterns.EMAIL_ADDRESS

                if (et_id.length() == 0){
                    showErrorMsg(et_id, tv_error_id, resources.getString(R.string.error_input_email), linear_id)
                } else if (!pattern.matcher(et_id.text).matches()){
                    showErrorMsg(et_id, tv_error_id, resources.getString(R.string.again_check), linear_id)
                } else{
                    hideErrorMsg(et_id, tv_error_id)
                }
            }
        }

        // Pw 입력란 포커스 잃었을 때,
        et_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.length() == 0){
                    showErrorMsg(et_pw, tv_error_pw, resources.getString(R.string.error_input_password), linear_pw)
                } else{
                    hideErrorMsg(et_pw, tv_error_pw)
                }
            }
        }

        // 로그인 API 호출
        btn_login.setOnClickListener {
            showProgress()
            viewModel.checkLogin(UserLogin(et_id.text.toString(), et_pw.text.toString()))
        }

        // 로그인 상태
        viewModel.getStateLogin().observe(this) { status ->
            status?.let {
                dismissProgress()
                if (it == Constant.ApiState.DONE) {
                    var intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    showErrorMsg(
                        et_id,
                        tv_error_id,
                        resources.getString(R.string.again_check),
                        linear_id
                    )
                    showErrorMsg(
                        et_pw,
                        tv_error_pw,
                        resources.getString(R.string.again_check),
                        linear_pw
                    )
                }
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

    private fun showProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.showProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun dismissProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.dismissProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    companion object{
        lateinit var context: Context

        fun showErrorMsg(et: EditText, tvMsg: TextView, msg: String, shakeLayout: LinearLayout){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_stroke_box, null)
            tvMsg.visibility = View.VISIBLE
            tvMsg.text = msg

            // 흔들림 효과
            YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(shakeLayout)
        }

        fun hideErrorMsg(et: EditText, tvMsg: TextView){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_box, null)
            tvMsg.visibility = View.INVISIBLE
        }
    }
}