package com.infinity.omos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.infinity.omos.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentToday by lazy { TodayFragment() }
    private val fragmentMyRecord by lazy { MyRecordFragment() }
    private val fragmentAllRecords by lazy { AllRecordFragment() }
    private val fragmentMyDj by lazy { MyDjFragment() }
    private val fragmentMyPage by lazy { MyPageFragment() }

    private var stateWrite = false

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
        bottom_nav.itemIconTintList = null // 아이콘 변경 시 색상 적용되는 문제 해결
        bottom_nav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_today -> {
                        toolbar.title = ""
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        stateWrite = false
                        invalidateOptionsMenu()
                        changeFragment(fragmentToday)
                        item.setIcon(R.drawable.ic_selected_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)
                    }
                    R.id.menu_myrecord -> {
                        toolbar.title = "MY 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = true
                        invalidateOptionsMenu()
                        changeFragment(fragmentMyRecord)
                        item.setIcon(R.drawable.ic_selected_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)
                    }
                    R.id.menu_allrecords -> {
                        toolbar.title = "전체 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        invalidateOptionsMenu()
                        changeFragment(fragmentAllRecords)
                        item.setIcon(R.drawable.ic_selected_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)
                    }
                    R.id.menu_mydj -> {
                        toolbar.title = "MY DJ"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        invalidateOptionsMenu()
                        changeFragment(fragmentMyDj)
                        item.setIcon(R.drawable.ic_selected_mydj)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)
                    }
                    R.id.menu_mypage -> {
                        toolbar.title = "MY 페이지"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        invalidateOptionsMenu()
                        changeFragment(fragmentMyPage)
                        item.setIcon(R.drawable.ic_selected_mypage)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
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
        var actionWrite = menu.findItem(R.id.action_write)
        actionWrite.isVisible = stateWrite
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