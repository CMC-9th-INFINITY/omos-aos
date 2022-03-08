package com.infinity.omos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity.omos.adapters.ViewPagerAdapter
import com.infinity.omos.databinding.ActivityMainBinding
import com.infinity.omos.ui.bottomnav.*
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    // Bottom Navigation
    private val fragmentToday by lazy { TodayFragment() }
    private val fragmentMyRecord by lazy { MyRecordFragment() }
    private val fragmentAllRecords by lazy { AllRecordFragment() }
    private val fragmentMyDj by lazy { MyDjFragment() }
    private val fragmentMyPage by lazy { MyPageFragment() }

    private var stateWrite = false
    private var stateNoti = false
    private var stateSearch = false
    private var prevTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        Log.d("MainActivity", "aToken: ${GlobalApplication.prefs.getString("accessToken")}")
        Log.d("MainActivity", "rToken: ${GlobalApplication.prefs.getString("refreshToken")}")
        Log.d("MainActivity", "userId: ${GlobalApplication.prefs.getLong("userId")}")

        initToolBar()
        initNavigationBar()
        initTabLayout()

        // 검색뷰 취소 버튼 클릭 시
        btn_cancel.setOnClickListener {
            linear.visibility = View.VISIBLE
            searchView.visibility = View.GONE
            searchTab.visibility = View.GONE
            viewPager.currentItem = 0
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
                if (et_search.length() > 0 || searchTab.visibility == View.VISIBLE){
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

        // 검색 완료
        et_search.setOnKeyListener { view, i, keyEvent ->
            when(i){
                KeyEvent.KEYCODE_ENTER -> {
                    if (keyEvent.action != KeyEvent.ACTION_DOWN){
                        if (et_search.length() > 0){
                            keyword = et_search.text.toString()
                            var intent = Intent("SEARCH_UPDATE")
                            intent.putExtra("keyword", keyword)
                            sendBroadcast(intent)

                            rv_search.visibility = View.GONE
                            searchTab.visibility = View.VISIBLE
                        } else{
                            Toast.makeText(this, "텍스트를 입력하세요.",Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        // OMOS 텍스트 이미지 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_title_logo)
    }

    private fun initTabLayout(){
        val viewpagerFragmentAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewpagerFragmentAdapter
        viewPager.isUserInputEnabled = false
        val tabTitles = listOf("전체", "노래", "앨범", "아티스트")
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = tabTitles[position] }.attach()
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
                        stateSearch = false
                        binding.lnToolbar.visibility = View.GONE
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
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
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
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
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
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
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
                        stateSearch = false
                        binding.lnToolbar.visibility = View.VISIBLE
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
        var actionSearch = menu.findItem(R.id.action_search)
        actionWrite.isVisible = stateWrite
        actionSearch.isVisible = stateSearch
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
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

    companion object {
        var keyword = ""
    }
}