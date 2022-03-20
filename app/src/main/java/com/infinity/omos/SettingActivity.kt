package com.infinity.omos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.infinity.omos.databinding.ActivitySettingBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.MyReceiver
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.toolbar
import kotlinx.android.synthetic.main.fragment_my_page.*
import java.util.*

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        initToolBar()

        btn_logout.setOnClickListener {

            val dlg = CustomDialog(this)
            dlg.show("정말 로그아웃 하시겠어요?", "로그아웃")

            dlg.setOnOkClickedListener { content ->
                when (content) {
                    "yes" -> {
                        UserApiClient.instance.logout { error ->
                            if (error != null){
                                Log.d("MyPage", "이메일 로그아웃")
                            } else{
                                Log.d("MyPage", "카카오 로그아웃")
                            }

                            Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                            GlobalApplication.prefs.setUserToken(null, null, -1)
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        val alarmManager = this?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, Constant.NOTI_ID, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val state = GlobalApplication.prefs.getString("alarm")
        if (state == "on"){
            btn_alarm.isChecked = true
        }

        btn_alarm.setOnCheckedChangeListener { _, b ->
            val toastMessage = if (b) {
                val repeatInterval = AlarmManager.INTERVAL_DAY
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY,21)
                }

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval,
                    pendingIntent
                )

                GlobalApplication.prefs.setString("alarm", "on")
                "알림이 발생합니다."
            } else {
                alarmManager.cancel(pendingIntent)
                GlobalApplication.prefs.setString("alarm", "off")
                "알림 예약을 취소하였습니다."
            }

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initToolBar(){
        toolbar.title = "설정"
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