package com.infinity.omos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.ActivityMyScrapRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.MyScrapRecordViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class MyScrapRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScrapRecordBinding
    private val viewModel: MyScrapRecordViewModel by viewModels()
    lateinit var broadcastReceiver: BroadcastReceiver

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_scrap_record)
        scrapBroadcastReceiver()

        initToolBar()

        val mAdapter = MyRecordListAdapter(this)
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.setScrapRecord(userId)
        viewModel.getScrapRecord().observe(this) { record ->
            record?.let {
                mAdapter.setDjRecord(it)

                if (it.isEmpty()){
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getStateScrapRecord().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.lnNorecord.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        // 검색뷰 취소 버튼 클릭 시
        binding.btnCancel.setOnClickListener {
            cancelSearch()
        }

        // EditText 초기화
        binding.btnRemove.setOnClickListener {
            binding.etSearch.setText("")
        }

        // 검색 리스트 노출
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mAdapter.filter.filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun cancelSearch() {
        binding.searchView.visibility = View.GONE
        binding.toolbar.visibility = View.VISIBLE

        binding.etSearch.setText("")

        // 키보드 내리기
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
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
                binding.toolbar.visibility = View.GONE
                binding.searchView.visibility = View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun scrapBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.setScrapRecord(userId)
            }
        }

        this.registerReceiver(
            broadcastReceiver,
            IntentFilter("SCRAP_UPDATE")
        )
    }
}