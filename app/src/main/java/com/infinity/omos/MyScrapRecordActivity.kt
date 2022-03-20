package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.infinity.omos.databinding.ActivityMyScrapRecordBinding
import kotlinx.android.synthetic.main.activity_register_nick.*

class MyScrapRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScrapRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_scrap_record)

        initToolBar()
    }

    private fun initToolBar(){
        toolbar.title = "스크랩한 레코드"
       setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action, menu)
        val actionWrite = menu.findItem(R.id.action_write)
        actionWrite.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}