package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentToday by lazy {TodayFragment()}
    private val fragmentMyRecord by lazy {MyRecordFragment()}
    private val fragmentAllRecords by lazy {AllRecordFragment()}
    private val fragmentMyDj by lazy {MyDjFragment()}
    private val fragmentMyPage by lazy {MyPageFragment()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()
    }

    private fun initNavigationBar(){
        bottom_nav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_today -> {
                        changeFragment(fragmentToday)
                    }
                    R.id.menu_myrecord -> {
                        changeFragment(fragmentMyRecord)
                    }
                    R.id.menu_allrecords -> {
                        changeFragment(fragmentAllRecords)
                    }
                    R.id.menu_mydj -> {
                        changeFragment(fragmentMyDj)
                    }
                    R.id.menu_mypage -> {
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
}