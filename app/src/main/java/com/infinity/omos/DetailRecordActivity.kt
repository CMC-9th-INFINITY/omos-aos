package com.infinity.omos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.infinity.omos.adapters.LyricsListAdapter
import com.infinity.omos.databinding.ActivityDetailRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.etc.GlobalFunction.Companion.changeList
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.ShareInstagram
import com.infinity.omos.viewmodels.DetailRecordViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class DetailRecordActivity : AppCompatActivity() {

    private val viewModel: DetailRecordViewModel by viewModels()
    private lateinit var binding: ActivityDetailRecordBinding
    private lateinit var dlg: CustomDialog

    private val userId = GlobalApplication.prefs.getInt("userId")
    private lateinit var actionInsta: MenuItem
    private lateinit var actionMore: MenuItem

    private var heart = false
    private var star = false
    private var prevHeart = false
    private var prevStar = false
    private var heartCnt = 0
    private var starCnt = 0
    private var toUserId = -1

    private var postId = -1
    private var stateCategory = ""

    private var musicId = ""
    private var musicTitle = ""
    private var artists = ""
    private var albumImageUrl = ""
    private var lyrics = ""
    private var interpret = ""
    private var recordTitle = ""
    private var recordContents = ""
    private var recordImageUrl = ""
    private var isPublic: Boolean? = null

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_record)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        dlg = CustomDialog(this)
        initializeBroadcastReceiver()

        postId = intent.getIntExtra("postId", -1)

        viewModel.setDetailRecord(postId, userId)
        viewModel.getDetailRecord().observe(this) { record ->
            record?.let {
                binding.data = it
                artists = GlobalFunction.setArtist(it.music.artists)
                binding.tvArtist.text = artists
                binding.tvDate.text = GlobalFunction.setDate(it.createdDate)
                binding.tvCategory.text = GlobalFunction.categoryEngToKr(this, it.category)
                binding.tvHeartCnt.text = String.format("%03d", it.likeCnt)
                binding.tvStarCnt.text = String.format("%03d", it.scrapCnt)

                prevHeart = it.isLiked
                prevStar = it.isScraped
                heartCnt = it.likeCnt
                starCnt = it.scrapCnt
                toUserId = it.userId

                stateCategory = it.category
                recordTitle = it.recordTitle
                recordContents = it.recordContents
                recordImageUrl = it.recordImageUrl
                isPublic = it.isPublic
                albumImageUrl = it.music.albumImageUrl
                musicTitle = it.music.musicTitle

                // 좋아요 상태
                heart = if (it.isLiked) {
                    binding.imgHeart.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_checked_heart
                        )
                    )
                    binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                    true
                } else {
                    binding.imgHeart.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_unchecked_heart
                        )
                    )
                    binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                    false
                }

                // 스크랩 상태
                star = if (it.isScraped) {
                    binding.imgStar.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_checked_star
                        )
                    )
                    binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                    true
                } else {
                    binding.imgStar.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_unchecked_star
                        )
                    )
                    binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                    false
                }

                // 공개/비공개 아이콘 변경
                if (it.isPublic == false) {
                    binding.btnPublic.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_private
                        )
                    )
                } else {
                    binding.btnPublic.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_public
                        )
                    )
                }

                // 한 줄 감상 구분
                when (it.category) {
                    "A_LINE" -> {
                        binding.tvAlineContents.visibility = View.VISIBLE
                        binding.tvRecordContents.visibility = View.GONE
                        binding.rvLyrics.visibility = View.GONE
                        binding.tvAlineContents.text = "\"${it.recordContents}\""
                    }

                    "LYRICS" -> {
                        binding.rvLyrics.visibility = View.VISIBLE
                        binding.tvAlineContents.visibility = View.GONE
                        binding.tvRecordContents.visibility = View.GONE
                        // 가사, 해석 분리
                        val contentsList = changeList(it.recordContents)
                        val lyricsList = ArrayList<String>()
                        val interpretList = ArrayList<String>()
                        for (i in contentsList.indices) {
                            if (i % 2 == 0) {
                                lyricsList.add(contentsList[i])
                            } else {
                                interpretList.add(contentsList[i])
                            }
                        }

                        for (i in lyricsList){
                            lyrics += i + "\n"
                        }

                        for (i in interpretList){
                            interpret += i + "\n"
                        }

                        val mAdapter = LyricsListAdapter(this)
                        mAdapter.setLyrics(lyricsList, false)
                        mAdapter.setInterpret(interpretList)
                        binding.rvLyrics.apply {
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(context)
                        }
                    }

                    else -> {
                        binding.tvRecordContents.visibility = View.VISIBLE
                        binding.tvAlineContents.visibility = View.GONE
                        binding.rvLyrics.visibility = View.GONE
                    }
                }

                // 내 레코드인지 확인
                if (it.userId == userId) {
                    binding.btnPublic.visibility = View.VISIBLE
                    binding.btnReport.visibility = View.GONE
                    actionInsta.isVisible = it.category == "A_LINE"
                    actionMore.isVisible = true
                } else {
                    binding.btnPublic.visibility = View.GONE
                    binding.btnReport.visibility = View.VISIBLE
                    actionInsta.isVisible = false
                    actionMore.isVisible = false
                }

                // 데이터 바인딩 되지도 않았는데 클릭하는 문제 해결
                actionInsta.isEnabled = true
                actionMore.isEnabled = true
            }
        }

        viewModel.getStateDetailRecord().observe(this) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.linearLayout.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.linearLayout.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {
                        Toast.makeText(this, "삭제된 레코드입니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent("RECORD_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                        finish()
                    }
                }
            }
        }

        viewModel.getStateDeleteRecord().observe(this) { state ->
            state?.let {
                dismissProgress()
                if (it.state) {
                    val intent = Intent("RECORD_UPDATE")
                    intent.putExtra("isDelete", true)
                    intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    sendBroadcast(intent)

                    val intent2 = Intent("PROFILE_UPDATE")
                    intent2.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    sendBroadcast(intent2)

                    finish()
                    Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 좋아요 클릭
        binding.btnHeart.setOnClickListener {
            if (heart) {
                binding.imgHeart.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_unchecked_heart
                    )
                )
                binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                heartCnt -= 1
                heart = false
            } else {
                binding.imgHeart.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_checked_heart
                    )
                )
                binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                heartCnt += 1
                heart = true
            }
            binding.tvHeartCnt.text = String.format("%03d", heartCnt)
        }

        // 스크랩 클릭
        binding.btnStar.setOnClickListener {
            if (star) {
                binding.imgStar.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_unchecked_star
                    )
                )
                binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                starCnt -= 1
                star = false
            } else {
                binding.imgStar.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_checked_star
                    )
                )
                binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                starCnt += 1
                star = true
            }
            binding.tvStarCnt.text = String.format("%03d", starCnt)
        }

        // DJ 클릭
        binding.btnDj.setOnClickListener {
            val intent = Intent(this, DjActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("toUserId", toUserId)
            startActivity(intent)
        }

        // 신고하기
        binding.btnReport.setOnClickListener {
            val dlg = CustomDialog(this)
            dlg.show("이 레코드를 신고하시겠어요?", "신고")

            dlg.setOnOkClickedListener { content ->
                when (content) {
                    "yes" -> {
                        viewModel.reportRecord(postId)
                        Toast.makeText(this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        initToolBar()
    }

    private fun initToolBar() {
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_record, menu)
        actionInsta = menu.findItem(R.id.action_insta)
        actionMore = menu.findItem(R.id.action_more)
        actionInsta.isEnabled = false
        actionMore.isEnabled = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insta -> {
                val insta = ShareInstagram(this)
                // visible 이 늦게 되는 문제 해결 (gone 은 빨리되는데)
                binding.imgLogo.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        insta.shareInsta(binding.linearLayout)
                        binding.imgLogo.visibility = View.GONE
                        binding.btnHeart.visibility = View.VISIBLE
                        binding.btnStar.visibility = View.VISIBLE
                        binding.imgLogo.viewTreeObserver.removeOnGlobalLayoutListener (this)
                    }
                })
                binding.imgLogo.visibility = View.VISIBLE
                binding.btnHeart.visibility = View.GONE
                binding.btnStar.visibility = View.GONE
                true
            }
            R.id.action_delete -> {
                val dlg = CustomDialog(this)
                dlg.show("삭제하시겠어요?", "삭제")

                dlg.setOnOkClickedListener { content ->
                    when (content) {
                        "yes" -> {
                            viewModel.deleteRecord(postId)
                            showProgress()
                        }
                    }
                }
                true
            }
            R.id.action_modify -> {
                when(stateCategory){
                    "LYRICS" -> {
                        val intent = Intent(this, WriteLyricsActivity::class.java)
                        intent.putExtra("musicId", musicId)
                        intent.putExtra("musicTitle", musicTitle)
                        intent.putExtra("artists", artists)
                        intent.putExtra("albumImageUrl", albumImageUrl)
                        intent.putExtra("category", GlobalFunction.categoryEngToKr(this, stateCategory))
                        intent.putExtra("modify", true)
                        intent.putExtra("recordTitle", recordTitle)
                        intent.putExtra("recordImageUrl", recordImageUrl)
                        intent.putExtra("lyrics", lyrics)
                        intent.putExtra("interpret", interpret)
                        intent.putExtra("isPublic", isPublic)
                        intent.putExtra("postId", postId)
                        startActivity(intent)
                    }

                    else -> {
                        val intent = Intent(this, WriteRecordActivity::class.java)
                        intent.putExtra("musicId", musicId)
                        intent.putExtra("musicTitle", musicTitle)
                        intent.putExtra("artists", artists)
                        intent.putExtra("albumImageUrl", albumImageUrl)
                        intent.putExtra("category", GlobalFunction.categoryEngToKr(this, stateCategory))
                        intent.putExtra("modify", true)
                        intent.putExtra("recordTitle", recordTitle)
                        intent.putExtra("recordContents", recordContents)
                        intent.putExtra("recordImageUrl", recordImageUrl)
                        intent.putExtra("isPublic", isPublic)
                        intent.putExtra("postId", postId)
                        startActivity(intent)
                    }
                }
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 좋아요, 스크랩 처리
        if (heart != prevHeart) {
            if (heart) {
                viewModel.saveLike(postId, userId)
            } else {
                viewModel.deleteLike(postId, userId)
            }

            val intent = Intent("LIKE_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
        }

        if (star != prevStar) {
            if (star) {
                viewModel.saveScrap(postId, userId)
            } else {
                viewModel.deleteScrap(postId, userId)
            }
            val intent = Intent("SCRAP_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
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
                val isDelete = intent?.getBooleanExtra("isDelete", false)
                if (isDelete != true){
                    viewModel.setDetailRecord(postId, userId)
                }
            }
        }

        this.registerReceiver(
            broadcastReceiver,
            IntentFilter("RECORD_UPDATE")
        )
    }
}