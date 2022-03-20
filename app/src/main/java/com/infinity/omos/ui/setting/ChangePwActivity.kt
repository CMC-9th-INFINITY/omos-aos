package com.infinity.omos.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.infinity.omos.R
import com.infinity.omos.databinding.ActivityChangePwBinding
import com.infinity.omos.viewmodels.ChangePwViewModel
import kotlinx.android.synthetic.main.activity_register.*

class ChangePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePwBinding
    private val viewModel: ChangePwViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pw)

        initToolBar()
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