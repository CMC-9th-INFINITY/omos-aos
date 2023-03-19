package com.infinity.omos.ui.record

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.R
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.databinding.ActivityCategoryBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.OmosApplication
import com.infinity.omos.viewmodels.CategoryViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_register.toolbar

class CategoryRecordsActivity : AppCompatActivity() {

    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var dlg: CustomDialog

    private var ctg = ""

    private var page = 0
    private val pageSize = 5
    private var isLoading = false

    private var postId = -1
    private lateinit var mAdapter: DetailCategoryListAdapter

    private val userId = OmosApplication.prefs.getInt("userId")

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var context = this
        dlg = CustomDialog(this)

        var category = intent.getStringExtra("category")
        initToolBar(category!!)
        initializeBroadcastReceiver()

        // 카테고리 Record 가져오기
        when (category) {
            resources.getString(R.string.a_line) -> {
                ctg = "A_LINE"
            }
            resources.getString(R.string.ost) -> {
                ctg = "OST"
            }
            resources.getString(R.string.story) -> {
                ctg = "STORY"
            }
            resources.getString(R.string.lyrics) -> {
                ctg = "LYRICS"
            }
            else -> {
                ctg = "FREE"
            }
        }

        mAdapter = DetailCategoryListAdapter(this)
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.getStateReportRecord().observe(this) { state ->
            state?.let {
                dismissProgress()
                if (it.state) {
                    val intent = Intent("RECORD_UPDATE")
                    intent.putExtra("isDelete", true)
                    intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    sendBroadcast(intent)

                    Toast.makeText(this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        mAdapter.setItemClickListener(object: DetailCategoryListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, postId: Int) {
                val dlg = CustomDialog(context)
                dlg.show("이 레코드를 신고하시겠어요?", "신고")

                dlg.setOnOkClickedListener { content ->
                    when(content){
                        "yes" -> {
                            viewModel.reportObject(userId, postId, null, null, "Record")
                            showProgress()
                        }
                    }
                }
            }
        })

        viewModel.setCategoryRecord(ctg, null, pageSize, "date", userId)
        viewModel.getCategoryRecord().observe(this, Observer { category ->
            category?.let {
                isLoading = if (it.isEmpty()) {
                    mAdapter.notifyItemRemoved(mAdapter.itemCount)
                    true
                } else {
                    mAdapter.addCategory(it)
                    mAdapter.notifyItemRangeInserted(page * pageSize, it.size)
                    postId = it[it.lastIndex].recordId
                    false
                }
            }
        })

        viewModel.getStateCategory().observe(this, Observer { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        if (page == 0){
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }

                    Constant.ApiState.DONE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {

                    }
                }
            }
        })

        // 무한 스크롤
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading && itemTotalCount > -1) {
                    isLoading = true
                    mAdapter.deleteLoading()
                    page ++
                    viewModel.setCategoryRecord(ctg, postId, pageSize, "date", userId)
                }
            }
        })

        // 스와이프 기능
        binding.swipeRefreshLayout.setOnRefreshListener {
            setSortRecord("date")
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.showProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun dismissProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.dismissProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setSortRecord("date")
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        this.registerReceiver(
            broadcastReceiver,
            IntentFilter("RECORD_UPDATE")
        )
    }

    private fun initToolBar(category: String){
        toolbar.title = category
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.sort_new -> {
                setSortRecord("date")
                Toast.makeText(this, "최신순", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.sort_scrap -> {
                setSortRecord("like")
                Toast.makeText(this, "공감순", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.sort_random -> {
                setSortRecord("random")
                Toast.makeText(this, "랜덤순", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSortRecord(sortType: String){
        page = 0
        postId = -1
        isLoading = false
        binding.recyclerView.scrollToPosition(0)
        mAdapter.clearCategory()
        viewModel.setCategoryRecord(ctg, null, pageSize, sortType, userId)
    }

    override fun onDestroy() {
        super.onDestroy()

        // 좋아요, 스크랩 처리
        var stateLike = false
        val saveHeartList = mAdapter.getSaveHeart()
        for (i in saveHeartList){
            viewModel.saveLike(i, userId)
            stateLike = true
        }

        val deleteHeartList = mAdapter.getDeleteHeart()
        for (i in deleteHeartList){
            viewModel.deleteLike(i, userId)
            stateLike = true
        }

        var stateScrap = false
        val saveScrapList = mAdapter.getSaveScrap()
        for (i in saveScrapList){
            viewModel.saveScrap(i, userId)
            stateScrap = true
        }

        val deleteScrapList = mAdapter.getDeleteScrap()
        for (i in deleteScrapList){
            viewModel.deleteScrap(i, userId)
            stateScrap = true
        }

        if (stateLike){
            val intent = Intent("LIKE_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
        }
        if (stateScrap){
            val intent = Intent("SCRAP_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
        }
    }
}