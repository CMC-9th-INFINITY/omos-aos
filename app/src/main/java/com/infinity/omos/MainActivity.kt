package com.infinity.omos

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
import com.infinity.omos.adapters.SearchListAdapter
import com.infinity.omos.adapters.ViewPagerAdapter
import com.infinity.omos.databinding.ActivityMainBinding
import com.infinity.omos.service.MyFirebaseMessagingService
import com.infinity.omos.support.PermissionSupport
import com.infinity.omos.ui.bottomnav.*
import com.infinity.omos.ui.searchtab.AllFragment
import com.infinity.omos.utils.BackKeyHandler
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.InAppUpdate
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


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

        // ????????? ?????? ??? ????????? ???????????? ??????
        // ???????????? ??? ??? ????????????
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
                Log.d("AppUpdate", "Update flow failed! Result code: $resultCode") // ????????? ?????? ??????
                Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_LONG).show()
                finishAffinity(); // ??? ??????
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        Log.d("MainActivity", "aToken: ${GlobalApplication.prefs.getString("accessToken")}")
        Log.d("MainActivity", "rToken: ${GlobalApplication.prefs.getString("refreshToken")}")
        Log.d("MainActivity", "userId: ${GlobalApplication.prefs.getInt("userId")}")

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

        // ????????? ??????
        binding.btnFloating.setOnClickListener {
            binding.btnFloating.visibility = View.GONE
            onOptionsItemSelected(actionWrite)
        }

        // ????????? ?????? ?????? ?????? ???
        binding.btnCancel.setOnClickListener {
            cancelSearch()
        }

        // EditText ?????????
        btn_remove.setOnClickListener {
            binding.etSearch.setText("")
        }

        fragmentToday.setItemClickListener(object: TodayFragment.OnItemClickListener{
            override fun onClick() {
                binding.btnFloating.visibility = View.GONE
                onOptionsItemSelected(actionWrite)
            }
        })

        val sAdapter = SearchListAdapter(this)
        binding.rvSearch.apply {
            adapter = sAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // ?????? ????????? ?????? ??? editText ??????
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

        // ?????? ????????? ??????
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isMusicSearch) {
                    if (binding.etSearch.length() > 0){
                        // ????????? ??????
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
                    // MY ????????? ?????? (????????????)
                    filterListener.onFilter(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        // ?????? ??????
        et_search.setOnKeyListener { _, i, keyEvent ->
            when (i) {
                KeyEvent.KEYCODE_ENTER -> {
                    if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                        if (isMusicSearch) {
                            if (et_search.length() > 0) {
                                keyword = et_search.text.toString()
                                var intent = Intent("SEARCH_UPDATE")
                                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                                intent.putExtra("keyword", keyword)
                                sendBroadcast(intent)

                                binding.rvSearch.visibility = View.GONE
                                binding.searchTab.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show()
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

        toolbar.title = ""
        setSupportActionBar(toolbar) // ?????? ??????

        // OMOS ????????? ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_title_logo)
    }

    private fun initTabLayout() {
        val viewpagerFragmentAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewpagerFragmentAdapter
        viewPager.isUserInputEnabled = false
        val tabTitles = listOf("??????", "??????", "??????", "????????????")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // ????????? ?????? ??? ??????
        val allFragment = viewpagerFragmentAdapter.getFragment()
        allFragment.setCurrentItem(object : AllFragment.OnItemClickListener {
            override fun setCurrentItem(position: Int) {
                viewPager.currentItem = position
            }
        })
    }

    private fun initNavigationBar() {
        binding.bottomNav.itemIconTintList = null // ????????? ?????? ??? ?????? ???????????? ?????? ??????
        binding.bottomNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_today -> {
                        toolbar.title = ""
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        stateWrite = false
                        stateSearch = false
                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark))
                        invalidateOptionsMenu()
                        changeFragment("Today", fragmentToday)
                        item.setIcon(R.drawable.ic_selected_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords)
                            .setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }

                        binding.lnToolbar.visibility = View.GONE
                        binding.btnFloating.visibility = View.VISIBLE
                    }
                    R.id.menu_myrecord -> {
                        toolbar.title = "MY ?????????"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = true
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyRecord", fragmentMyRecord)
                        item.setIcon(R.drawable.ic_selected_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_allrecords)
                            .setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.menu_allrecords -> {
                        toolbar.title = "?????? ?????????"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = true
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("AllRecords", fragmentAllRecords)
                        item.setIcon(R.drawable.ic_selected_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.menu_mydj -> {
                        toolbar.title = "MY DJ"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = false
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark))
                        binding.divisor.visibility = View.VISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyDJ", fragmentMyDj)
                        item.setIcon(R.drawable.ic_selected_mydj)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords)
                            .setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mypage).setIcon(R.drawable.ic_mypage)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                    R.id.menu_mypage -> {
                        toolbar.title = "MY ?????????"
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        stateWrite = false
                        stateSearch = false
                        binding.lnToolbar.visibility = View.VISIBLE
                        binding.btnFloating.visibility = View.GONE

                        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.deep_dark))
                        binding.divisor.visibility = View.INVISIBLE

                        invalidateOptionsMenu()
                        changeFragment("MyPage", fragmentMyPage)
                        item.setIcon(R.drawable.ic_selected_mypage)
                        bottom_nav.menu.findItem(R.id.menu_today).setIcon(R.drawable.ic_today)
                        bottom_nav.menu.findItem(R.id.menu_myrecord).setIcon(R.drawable.ic_myrecord)
                        bottom_nav.menu.findItem(R.id.menu_allrecords)
                            .setIcon(R.drawable.ic_allrecords)
                        bottom_nav.menu.findItem(R.id.menu_mydj).setIcon(R.drawable.ic_mydj)

                        if (binding.searchView.visibility == View.VISIBLE) {
                            cancelSearch()
                        }
                    }
                }
                true
            }
            selectedItemId = R.id.menu_today // ?????? ???????????????
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
                if (binding.bottomNav.selectedItemId == R.id.menu_myrecord) {
                    binding.lnToolbar.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE
                    isMusicSearch = false
                } else {
                    binding.lnToolbar.visibility = View.GONE
                    binding.frameLayout.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE

                    if (binding.bottomNav.selectedItemId != R.id.menu_mydj) {
                        binding.lnRanking.visibility = View.VISIBLE
                    }

                    isMusicSearch = true
                }
                isWrite = false

                // editText ????????? ??????
                et_search.isFocusableInTouchMode = true
                et_search.requestFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(et_search, 0)

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

                // editText ????????? ??????
                et_search.isFocusableInTouchMode = true
                et_search.requestFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(et_search, 0)

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

        // today??? ?????? ??????
        if (binding.bottomNav.selectedItemId != R.id.menu_today) {
            binding.lnToolbar.visibility = View.VISIBLE
        } else {
            binding.btnFloating.visibility = View.VISIBLE
        }

        viewPager.currentItem = 0
        et_search.setText("")

        // ????????? ?????????
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(et_search.windowToken, 0)
    }

    // ???????????? ?????????
    private val backKeyHandler = BackKeyHandler(this)
    override fun onBackPressed() {
        if (binding.searchView.visibility == View.VISIBLE){
            // ????????? ??????????????? ??????
            cancelSearch()
        } else{
            // ?????? ???????????? ?????? ??? ??? ??????
            backKeyHandler.onBackPressed()
        }
    }

    private fun permissionCheck(){
        // SDK 23?????? ??????????????? Permission ?????? x
        if (Build.VERSION.SDK_INT >= 23) {
            permission = PermissionSupport(this, this)
            if (!permission.checkPermission()) {
                // ????????? ?????????,
                permission.requestPermission() // ?????? ??????
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
            Toast.makeText(this, "?????? ???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        var keyword = ""
        var isWrite = false
        lateinit var bottomNav: BottomNavigationView
    }
}