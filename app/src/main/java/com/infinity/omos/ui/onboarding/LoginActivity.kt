package com.infinity.omos.ui.onboarding

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
import com.infinity.omos.utils.InAppUpdate
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

    private lateinit var inAppUpdate: InAppUpdate
    private val REQUEST_CODE_UPDATE = 100

    override fun onResume() {
        super.onResume()
        inAppUpdate.checkInProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.d("AppUpdate", "Update flow failed! Result code: $resultCode") // ????????? ?????? ??????
                Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_LONG).show()
                finishAffinity(); // ??? ??????
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inAppUpdate = InAppUpdate(this)
        inAppUpdate.checkInAppUpdate()

        dlg = CustomDialog(this)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,
            R.layout.activity_login
        )
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // ???????????? ?????? ON/OFF
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

        // ????????? ?????? ?????????
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

        // ?????? ????????? ???????????? ??????
        viewModel.getStateSnsLogin().observe(this, Observer { state ->
            state?.let {
                dismissProgress()
                when(it){
                    Constant.ApiState.DONE -> {
                        Log.d("LoginActivity", "?????? ????????? ???????????????.")
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "???????????? ????????? ??????????????????.", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        Log.d("LoginActivity", "??????????????? ???????????????.")
                        val intent = Intent(this, RegisterNickActivity::class.java)
                        intent.putExtra("sns", true)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })

        // ?????? ????????? ??????
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "????????? ?????? ???(?????? ??????)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "???????????? ?????? ???", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "?????? ????????? ???????????? ?????? ????????? ??? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "?????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "???????????? ?????? scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "????????? ???????????? ??????(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "?????? ?????? ????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        dismissProgress()
                        Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "?????? ??????")
                    }
                }
            }
            else if (token != null) {
                // ?????? ????????? ?????? ?????? ??? ?????? ????????? ???????????? ??????
                UserApiClient.instance.me { user, _ ->
                    userId = user?.id.toString()+"@kakao.com"
                    viewModel.checkSnsLogin(UserSnsLogin(userId))
                }
            }
        }

        // Id ????????? ????????? ????????? ???,
        et_id.setOnFocusChangeListener { _, b ->
            if (!b){
                val pattern: Pattern = Patterns.EMAIL_ADDRESS

                if (et_id.length() == 0){
                    showErrorMsg(this, et_id, tv_error_id, resources.getString(R.string.error_input_email), linear_id)
                } else if (!pattern.matcher(et_id.text).matches()){
                    showErrorMsg(this, et_id, tv_error_id, resources.getString(R.string.again_check), linear_id)
                } else{
                    hideErrorMsg(this, et_id, tv_error_id)
                }
            }
        }

        // Pw ????????? ????????? ????????? ???,
        et_pw.setOnFocusChangeListener { view, b ->
            if (!b){
                if (et_pw.length() == 0){
                    showErrorMsg(this, et_pw, tv_error_pw, resources.getString(R.string.error_input_password), linear_pw)
                } else{
                    hideErrorMsg(this, et_pw, tv_error_pw)
                }
            }
        }

        // ????????? API ??????
        binding.btnLogin.setOnClickListener {
            showProgress()
            viewModel.checkLogin(UserLogin(et_id.text.toString(), et_pw.text.toString()))
        }

        // ????????? ??????
        viewModel.getStateLogin().observe(this) { status ->
            status?.let {
                dismissProgress()
                when(it){
                    Constant.ApiState.DONE -> {
                        var intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                    Constant.ApiState.NETWORK -> {
                        Toast.makeText(this, "???????????? ????????? ??????????????????.", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        showErrorMsg(
                            this,
                            et_id,
                            tv_error_id,
                            resources.getString(R.string.again_check),
                            linear_id
                        )
                        showErrorMsg(
                            this,
                            et_pw,
                            tv_error_pw,
                            resources.getString(R.string.again_check),
                            linear_pw
                        )
                    }
                }
            }
        }

        // ????????? ?????? ????????? ????????? ??????
        binding.btnKakaoLogin.setOnClickListener {
            showProgress()
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

        // ???????????? ????????? ??????
        binding.btnRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ???????????? ?????? ????????? ??????
        binding.btnFindPw.setOnClickListener {
            val intent = Intent(this, FindPwActivity::class.java)
            startActivity(intent)
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

        fun showErrorMsg(context: Context, et: EditText, tvMsg: TextView, msg: String, shakeLayout: LinearLayout){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_stroke_box, null)
            tvMsg.visibility = View.VISIBLE
            tvMsg.text = msg

            // ????????? ??????
            YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(shakeLayout)
        }

        fun hideErrorMsg(context: Context, et: EditText, tvMsg: TextView){
            et.background = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.rectangle_box, null)
            tvMsg.visibility = View.INVISIBLE
        }
    }
}