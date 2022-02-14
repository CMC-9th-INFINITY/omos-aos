package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.infinity.omos.ui.*
import com.kakao.sdk.common.util.Utility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentToday by lazy { TodayFragment() }
    private val fragmentMyRecord by lazy { MyRecordFragment() }
    private val fragmentAllRecords by lazy { AllRecordFragment() }
    private val fragmentMyDj by lazy { MyDjFragment() }
    private val fragmentMyPage by lazy { MyPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
        initNavigationBar()
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        // OMOS 텍스트 이미지 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.omos_title)
    }

    private fun initNavigationBar(){
        bottom_nav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_today -> {
                        toolbar.title = ""
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        changeFragment(fragmentToday)
                    }
                    R.id.menu_myrecord -> {
                        toolbar.title = "MY 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        changeFragment(fragmentMyRecord)
                    }
                    R.id.menu_allrecords -> {
                        toolbar.title = "전체 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        changeFragment(fragmentAllRecords)
                    }
                    R.id.menu_mydj -> {
                        toolbar.title = "MY DJ"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        changeFragment(fragmentMyDj)
                    }
                    R.id.menu_mypage -> {
                        toolbar.title = "MY 페이지"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        changeFragment(fragmentMyPage)
                    }
                }
                true
            }
            selectedItemId = R.id.menu_today // 초기 프래그먼트
        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_noti -> {
                Toast.makeText(this, "Noti", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_search -> {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_write -> {
                Toast.makeText(this, "Write", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}