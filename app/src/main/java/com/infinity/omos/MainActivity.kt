package com.infinity.omos

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.infinity.omos.ui.bottomnav.*
import com.infinity.omos.utils.GlobalApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_register_nick.*

class MainActivity : AppCompatActivity() {
    private val fragmentToday by lazy { TodayFragment() }
    private val fragmentMyRecord by lazy { MyRecordFragment() }
    private val fragmentAllRecords by lazy { AllRecordFragment() }
    private val fragmentMyDj by lazy { MyDjFragment() }
    private val fragmentMyPage by lazy { MyPageFragment() }

    private var stateWrite = false
    private var stateNoti = false
    private var prevTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "aToken: ${GlobalApplication.prefs.getString("accessToken")}")
        Log.d("MainActivity", "rToken: ${GlobalApplication.prefs.getString("refreshToken")}")
        Log.d("MainActivity", "ID: ${GlobalApplication.prefs.getLong("userId")}")

        initToolBar()
        initNavigationBar()

        // 검색뷰 취소 버튼 클릭 시
        btn_cancel.setOnClickListener {
            linear.visibility = View.VISIBLE
            searchView.visibility = View.GONE
            et_search.setText("")

            // 키보드 내리기
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(et_search.windowToken, 0)
        }

        // EditText 초기화
        btn_remove.setOnClickListener {
            et_search.setText("")
        }

        // 검색 리스트 노출
        et_search.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_search.length() > 0){
                    ln_ranking.visibility = View.GONE
                    rv_search.visibility = View.VISIBLE
                } else{
                    ln_ranking.visibility = View.VISIBLE
                    rv_search.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
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
                        stateNoti = true
                        invalidateOptionsMenu()
                        changeFragment("Today", fragmentToday)
                        item.setIcon(R.drawable.ic_selected_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (searchView.visibility == View.VISIBLE){
                            searchView.visibility = View.GONE
                            linear.visibility = View.VISIBLE
                        }
                    }
                    R.id.menu_myrecord -> {
                        toolbar.title = "MY 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = true
                        stateNoti = false
                        invalidateOptionsMenu()
                        changeFragment("MyRecord", fragmentMyRecord)
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
                        stateNoti = false
                        invalidateOptionsMenu()
                        changeFragment("AllRecords", fragmentAllRecords)
                        item.setIcon(R.drawable.ic_selected_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (searchView.visibility == View.VISIBLE){
                            searchView.visibility = View.GONE
                            linear.visibility = View.VISIBLE
                        }
                    }
                    R.id.menu_mydj -> {
                        toolbar.title = "MY DJ"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateNoti = false
                        invalidateOptionsMenu()
                        changeFragment("MyDJ", fragmentMyDj)
                        item.setIcon(R.drawable.ic_selected_mydj)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (searchView.visibility == View.VISIBLE){
                            searchView.visibility = View.GONE
                            linear.visibility = View.VISIBLE
                        }
                    }
                    R.id.menu_mypage -> {
                        toolbar.title = "MY 페이지"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateNoti = false
                        invalidateOptionsMenu()
                        changeFragment("MyPage", fragmentMyPage)
                        item.setIcon(R.drawable.ic_selected_mypage)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords).setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)

                        if (searchView.visibility == View.VISIBLE){
                            searchView.visibility = View.GONE
                            linear.visibility = View.VISIBLE
                        }
                    }
                }
                true
            }
            selectedItemId = R.id.menu_today // 초기 프래그먼트
        }
    }

    private fun changeFragment(tag:String, fragment: Fragment){

        if (supportFragmentManager.findFragmentByTag(tag) == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.framelayout, fragment, tag)
                .commit()
        } else{
            supportFragmentManager
                .beginTransaction()
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commit()
        }

        if (prevTag != ""){
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(prevTag)!!)
                .commit()
        }

        prevTag = tag
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action, menu)
        var actionWrite = menu.findItem(R.id.action_write)
        var actionNoti = menu.findItem(R.id.action_noti)
        actionWrite.isVisible = stateWrite
        actionNoti.isVisible = stateNoti
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
                linear.visibility = View.GONE
                searchView.visibility = View.VISIBLE

                // editText 포커스 주기
                et_search.isFocusableInTouchMode = true
                et_search.requestFocus()
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(et_search,0)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}