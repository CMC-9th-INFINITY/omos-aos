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
                    else -> {}
                }
            }
        }

        // ????????? ?????? ?????? ?????? ???
        binding.btnCancel.setOnClickListener {
            cancelSearch()
        }

        // EditText ?????????
        binding.btnRemove.setOnClickListener {
            binding.etSearch.setText("")
        }

        // ?????? ????????? ??????
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

        // ????????? ?????????
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun initToolBar(){
        toolbar.title = "???????????? ?????????"
       setSupportActionBar(toolbar) // ?????? ??????

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

                // editText ????????? ??????
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

    override fun onBackPressed() {
        if (binding.searchView.visibility == View.VISIBLE){
            // ????????? ??????????????? ??????
            cancelSearch()
        } else{
            super.onBackPressed()
        }
    }
}