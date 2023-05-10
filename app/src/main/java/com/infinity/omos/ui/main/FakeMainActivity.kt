package com.infinity.omos.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.infinity.omos.OmosApplication
import com.infinity.omos.R
import com.infinity.omos.adapters.SearchListAdapter
import com.infinity.omos.adapters.ViewPagerAdapter
import com.infinity.omos.databinding.ActivityFakeMainBinding
import com.infinity.omos.service.MyFirebaseMessagingService
import com.infinity.omos.support.PermissionSupport
import com.infinity.omos.ui.main.today.TodayFragment
import com.infinity.omos.ui.search.AllFragment
import com.infinity.omos.utils.BackKeyHandler
import com.infinity.omos.utils.InAppUpdate
import com.infinity.omos.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FakeMainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityFakeMainBinding

    // Bottom Navigation
    private val fragmentToday by lazy { TodayFragment() }
    private val fragmentMyRecord by lazy { MyRecordFragment() }
    private val fragmentAllRecords by lazy { AllRecordFragment() }
    private val fragmentMyDj by lazy { MyDjFragment() }
    private val fragmentMyPage by lazy { MyPageFragment() }

    private var stateWrite = false
    private var stateNoti = false
    private var stateSearch = false

    private var isMusicSearch = false
    private var prevTag = ""

    private lateinit var actionSearch: MenuItem
    private lateinit var actionWrite: MenuItem

    lateinit var permission: PermissionSupport

    private var lastChange = 0L

    private lateinit var inAppUpdate: InAppUpdate
    private val REQUEST_CODE_UPDATE = 100

    private lateinit var filterListener: OnFilterListener

    interface OnFilterListener{
        fun onFilter(str: String)
    }

    fun setFilterListener(filterListener: OnFilterListener){
        this.filterListener = filterListener
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // 레코드 작성 시 검색뷰 사라지게 하기
        // 뒤로가기 할 땐 남아있음
        if (null != intent) {
            val state = intent.getBooleanExtra("save", false)
            if (state){
                cancelSearch()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.checkInProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.d("AppUpdate", "Update flow failed! Result code: $resultCode") // 로그로 코드 확인
                Toast.makeText(this, "업데이트를 진행해주세요.", Toast.LENGTH_LONG).show()
                finishAffinity(); // 앱 종료
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fake_main)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        Log.d("MainActivity", "aToken: ${OmosApplication.prefs.getString("accessToken")}")
        Log.d("MainActivity", "rToken: ${OmosApplication.prefs.getString("refreshToken")}")
        Log.d("MainActivity", "userId: ${OmosApplication.prefs.getInt("userId")}")

        inAppUpdate = InAppUpdate(this)
        inAppUpdate.checkInAppUpdate()

        permissionCheck()
        initToolBar()
        initNavigationBar()
        initTabLayout()
        bottomNav = binding.bottomNav

        // FCM
        val fcm = Intent(this, MyFirebaseMessagingService::class.java)
        startService(fcm)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("MainActivity", "Firebase Token: $it")
        }

        // 플로팅 버튼
        binding.btnFloating.setOnClickListener {
            binding.btnFloating.visibility = View.GONE
            onOptionsItemSelected(actionWrite)
        }

        // 검색뷰 취소 버튼 클릭 시
        binding.btnCancel.setOnClickListener {
            cancelSearch()
        }

        // EditText 초기화
        binding.btnRemove.setOnClickListener {
            binding.etSearch.setText("")
        }

        /*fragmentToday.setItemClickListener(object: FakeTodayFragment.OnItemClickListener{
            override fun onClick() {
                binding.btnFloating.visibility = View.GONE
                onOptionsItemSelected(actionWrite)
            }
        })*/

        val sAdapter = SearchListAdapter(this)
        binding.rvSearch.apply {
            adapter = sAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // 노래 리스트 클릭 시 editText 셋팅
        sAdapter.setItemClickListener(object: SearchListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, title: String) {

                binding.etSearch.setText(title)
                binding.etSearch.setSelection(title.length)

                keyword = binding.etSearch.text.toString()
                var intent = Intent("SEARCH_UPDATE")
                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                intent.putExtra("keyword", keyword)
                sendBroadcast(intent)

                binding.rvSearch.visibility = View.GONE
                binding.searchTab.visibility = View.VISIBLE
            }
        })

        viewModel.getSearchMusic().observe(this) { data ->
            data?.let {
                sAdapter.setMusicTitle(it)
            }
        }

        // 검색 리스트 노출
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isMusicSearch) {
                    if (binding.etSearch.length() > 0){
                        // 검색어 노출
                        binding.lnRanking.visibility = View.GONE
                        binding.rvSearch.visibility = View.VISIBLE

                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            if (System.currentTimeMillis() - lastChange >= 300){
                                keyword = binding.etSearch.text.toString()
                                viewModel.setSearchMusic(p0.toString(), 10, 0)
                            }
                        }, 300)
                        lastChange = System.currentTimeMillis()
                    } else{
                        binding.lnRanking.visibility = View.VISIBLE
                        binding.rvSearch.visibility = View.GONE
                        sAdapter.clearSearch()
                    }
                    binding.searchTab.visibility = View.GONE
                } else{
                    // MY 레코드 검색 (필터기능)
                    filterListener.onFilter(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        // 검색 완료
        binding.etSearch.setOnKeyListener { _, i, keyEvent ->
            when (i) {
                KeyEvent.KEYCODE_ENTER -> {
                    if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                        if (isMusicSearch) {
                            if (binding.etSearch.length() > 0) {
                                keyword = binding.etSearch.text.toString()
                                var intent = Intent("SEARCH_UPDATE")
                                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                                intent.putExtra("keyword", keyword)
                                sendBroadcast(intent)

                                binding.rvSearch.visibility = View.GONE
                                binding.searchTab.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(this, "텍스트를 입력하세요.", Toast.LENGTH_SHORT).show()
                            }
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

    private fun initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar) // 툴바 사용

        // OMOS 텍스트 이미지 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_omos_title_logo)
    }

    private fun initTabLayout() {
        val viewpagerFragmentAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewpagerFragmentAdapter
        binding.viewPager.isUserInputEnabled = false
        val tabTitles = listOf("전체", "노래", "앨범", "아티스트")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // 더보기 클릭 시 이동
        val allFragment = viewpagerFragmentAdapter.getFragment()
        allFragment.setCurrentItem(object : AllFragment.OnItemClickListener {
            override fun setCurrentItem(position: Int) {
                binding.viewPager.currentItem = position
            }
        })
    }

    private fun initNavigationBar() {
        binding.bottomNav.itemIconTintList = null // 아이콘 변경 시 색상 적용되는 문제 해결
        binding.bottomNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.today_fragment -> {
                        binding.toolbar.title = ""
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        stateWrite = false
                        stateSearch = false
                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.dark
                        ))
                        invalidateOptionsMenu()
                        changeFragment("Today", fragmentToday)
                        item.setIcon(R.drawable.ic_selected_today)
                        binding.bottomNav.menu.findItem(R.id.my_record_fragment).setIcon(R.drawable.ic_my_record)
                        binding.bottomNav.menu.findItem(R.id.all_records_fragment)
                            .setIcon(R.drawable.ic_all_records)
                        binding.bottomNav.menu.findItem(R.id.my_dj_fragment).setIcon(R.drawable.ic_my_dj)
                        binding.bottomNav.menu.findItem(R.id.my_page_fragment).setIcon(R.drawable.ic_my_page)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }

                        binding.lnToolbar.visibility = View.GONE
                        binding.btnFloating.visibility = View.VISIBLE
                    }
                    R.id.my_record_fragment -> {
                        binding.toolbar.title = "MY 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = true
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.dark
                        ))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyRecord", fragmentMyRecord)
                        item.setIcon(R.drawable.ic_selected_myrecord)
                        binding.bottomNav.menu.findItem(R.id.today_fragment).setIcon(R.drawable.ic_today)
                        binding.bottomNav.menu.findItem(R.id.all_records_fragment)
                            .setIcon(R.drawable.ic_all_records)
                        binding.bottomNav.menu.findItem(R.id.my_dj_fragment).setIcon(R.drawable.ic_my_dj)
                        binding.bottomNav.menu.findItem(R.id.my_page_fragment).setIcon(R.drawable.ic_my_page)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.all_records_fragment -> {
                        binding.toolbar.title = "전체 레코드"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.dark
                        ))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("AllRecords", fragmentAllRecords)
                        item.setIcon(R.drawable.ic_selected_allrecords)
                        binding.bottomNav.menu.findItem(R.id.today_fragment).setIcon(R.drawable.ic_today)
                        binding.bottomNav.menu.findItem(R.id.my_record_fragment).setIcon(R.drawable.ic_my_record)
                        binding.bottomNav.menu.findItem(R.id.my_dj_fragment).setIcon(R.drawable.ic_my_dj)
                        binding.bottomNav.menu.findItem(R.id.my_page_fragment).setIcon(R.drawable.ic_my_page)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.my_dj_fragment -> {
                        binding.toolbar.title = "MY DJ"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = false
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.dark
                        ))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyDJ", fragmentMyDj)
                        item.setIcon(R.drawable.ic_selected_mydj)
                        binding.bottomNav.menu.findItem(R.id.today_fragment).setIcon(R.drawable.ic_today)
                        binding.bottomNav.menu.findItem(R.id.my_record_fragment).setIcon(R.drawable.ic_my_record)
                        binding.bottomNav.menu.findItem(R.id.all_records_fragment)
                            .setIcon(R.drawable.ic_all_records)
                        binding.bottomNav.menu.findItem(R.id.my_page_fragment).setIcon(R.drawable.ic_my_page)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.my_page_fragment -> {
                        binding.toolbar.title = "MY 페이지"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = false
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.deep_dark
                        ))
                        binding.divisor.visibility = View.INVISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyPage", fragmentMyPage)
                        item.setIcon(R.drawable.ic_selected_mypage)
                        binding.bottomNav.menu.findItem(R.id.today_fragment).setIcon(R.drawable.ic_today)
                        binding.bottomNav.menu.findItem(R.id.my_record_fragment).setIcon(R.drawable.ic_my_record)
                        binding.bottomNav.menu.findItem(R.id.all_records_fragment)
                            .setIcon(R.drawable.ic_all_records)
                        binding.bottomNav.menu.findItem(R.id.my_dj_fragment).setIcon(R.drawable.ic_my_dj)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                }
                true
            }
            selectedItemId = R.id.today_fragment // 초기 프래그먼트
        }
    }

    private fun changeFragment(tag: String, fragment: Fragment) {

        if (prevTag != "") {
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(prevTag)!!)
                .commit()
        }

        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, fragment, tag)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commit()
        }

        prevTag = tag
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action, menu)
        actionWrite = menu.findItem(R.id.action_write)
        actionSearch = menu.findItem(R.id.action_search)
        actionWrite.isVisible = stateWrite
        actionSearch.isVisible = stateSearch

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                if (binding.bottomNav.selectedItemId == R.id.my_record_fragment) {
                    binding.lnToolbar.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE
                    isMusicSearch = false
                } else {
                    binding.lnToolbar.visibility = View.GONE
                    binding.frameLayout.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE

                    if (binding.bottomNav.selectedItemId != R.id.my_dj_fragment) {
                        binding.lnRanking.visibility = View.VISIBLE
                    }

                    isMusicSearch = true
                }
                isWrite = false

                // editText 포커스 주기
                binding.etSearch.isFocusableInTouchMode = true
                binding.etSearch.requestFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.etSearch, 0)

                true
            }
            R.id.action_write -> {
                binding.lnToolbar.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.searchView.visibility = View.VISIBLE
                binding.lnRanking.visibility = View.VISIBLE
                isWrite = true
                isMusicSearch = true

                // editText 포커스 주기
                binding.etSearch.isFocusableInTouchMode = true
                binding.etSearch.requestFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.etSearch, 0)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cancelSearch() {
        binding.searchView.visibility = View.GONE
        binding.lnRanking.visibility = View.GONE
        binding.searchTab.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.frameLayout.visibility = View.VISIBLE
        binding.bottomNav.visibility = View.VISIBLE

        // today는 툴바 없음
        if (binding.bottomNav.selectedItemId != R.id.today_fragment) {
            binding.lnToolbar.visibility = View.VISIBLE
        } else {
            binding.btnFloating.visibility = View.VISIBLE
        }

        binding.viewPager.currentItem = 0
        binding.etSearch.setText("")

        // 키보드 내리기
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    // 뒤로가기 핸들러
    private val backKeyHandler = BackKeyHandler(this)
    override fun onBackPressed() {
        if (binding.searchView.visibility == View.VISIBLE){
            // 검색뷰 열려있으면 닫기
            cancelSearch()
        } else{
            // 두번 뒤로가기 클릭 시 앱 종료
            backKeyHandler.onBackPressed()
        }
    }

    private fun permissionCheck(){
        // SDK 23버전 이하에서는 Permission 필요 x
        if (Build.VERSION.SDK_INT >= 23) {
            permission = PermissionSupport(this, this)
            if (!permission.checkPermission()) {
                // 권한이 없으면,
                permission.requestPermission() // 권한 요청
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            Toast.makeText(this, "권한 설정에서 권한을 허용하십시오.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        var keyword = ""
        var isWrite = false
        lateinit var bottomNav: BottomNavigationView
    }
}