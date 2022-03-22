package com.infinity.omos.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityDeleteAccountBinding
import com.infinity.omos.ui.bottomnav.MyPageFragment
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.ChangePwViewModel
import com.infinity.omos.viewmodels.DeleteAccountViewModel
import kotlinx.android.synthetic.main.activity_register.*

class DeleteAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteAccountBinding
    private val viewModel: DeleteAccountViewModel by viewModels()

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delete_account)
        binding.lifecycleOwner = this
        binding.tvNick.text = MyPageFragment.myNickname

        initToolBar()

        viewModel.getStateSignOut().observe(this) { state ->
            state?.let {
                if (it.state){
                    GlobalApplication.prefs.setUserToken(null, null, -1)

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    Toast.makeText(this, "계정이 탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnComplete.setOnClickListener {
            val dlg = CustomDialog(this)
            dlg.show("정말 탈퇴하시겠습니까?", "탈퇴")
            dlg.setOnOkClickedListener { content ->
                when(content){
                    "yes" -> {
                        viewModel.signOut(userId)
                    }
                }
            }
        }
    }

    private fun initToolBar(){
        toolbar.title = "계정 탈퇴"
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