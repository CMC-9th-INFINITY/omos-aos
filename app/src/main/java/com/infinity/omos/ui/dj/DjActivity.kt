package com.infinity.omos.ui.dj

import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.ActivityDjBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.DjViewModel
import kotlinx.android.synthetic.main.activity_register.*

class DjActivity : AppCompatActivity() {

    private val viewModel: DjViewModel by viewModels()
    private lateinit var binding: ActivityDjBinding

    private val fromUserId = GlobalApplication.prefs.getInt("userId")
    private var toUserId = -1
    private var isClickFollow = false
    private var followerCnt = 0
    lateinit var dlg: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dj)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        dlg = CustomDialog(this)

        toUserId = intent.getIntExtra("toUserId", -1)

        // 내 프로필이라면,
        if (fromUserId == toUserId){
            binding.btnFollow.visibility = View.GONE
        }

        initToolBar()

        val mAdapter = MyRecordListAdapter(this)
        binding.rvRecord.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.setDjProfile(fromUserId, toUserId)
        viewModel.getDjProfile().observe(this) { profile ->
            profile?.let {
                binding.data = it
                followerCnt = it.count.followerCount

                if (it.isFollowed){
                    followerCnt -= 1
                    setFollowing()
                } else{
                    followerCnt += 1
                    setFollow()
                }
            }
        }

        viewModel.setDjRecord(toUserId)
        viewModel.getDjRecord().observe(this) { record ->
            record?.let {
                mAdapter.setDjRecord(it)

                if (it.isEmpty()){
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getStateDjRecord().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.lnNorecord.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                    else -> {}
                }
            }
        }

        viewModel.getStateSaveFollow().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.DONE -> {
                        val intent = Intent("PROFILE_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                    }
                    else -> {}
                }
            }
        }

        viewModel.getStateDeleteFollow().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.DONE -> {
                        val intent = Intent("PROFILE_UPDATE")
                        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent)
                    }
                    else -> {}
                }
            }
        }

        viewModel.getStateBlockUser().observe(this) { state ->
            state?.let {
                when(it.state){
                    true -> {
                        dismissProgress()
                        val intent1 = Intent("RECORD_UPDATE")
                        intent1.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent1)

                        val intent2 = Intent("DJ_UPDATE")
                        intent2.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent2)

                        val intent3 = Intent("LIKE_UPDATE")
                        intent3.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent3)

                        val intent4 = Intent("SCRAP_UPDATE")
                        intent4.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent4)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.getStateReportUser().observe(this) { state ->
            state?.let {
                when(it.state){
                    true -> {
                        dismissProgress()
                        val intent1 = Intent("RECORD_UPDATE")
                        intent1.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent1)

                        val intent2 = Intent("DJ_UPDATE")
                        intent2.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent2)

                        val intent3 = Intent("LIKE_UPDATE")
                        intent3.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent3)

                        val intent4 = Intent("SCRAP_UPDATE")
                        intent4.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                        sendBroadcast(intent4)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnFollow.setOnClickListener {
            if (binding.btnFollow.text == "팔로우"){
                setFollowing()
                viewModel.saveFollow(fromUserId, toUserId)
            } else{
                setFollow()
                viewModel.deleteFollow(fromUserId, toUserId)
            }
            isClickFollow = true
        }
    }

    private fun setFollow(){
        binding.btnFollow.text = "팔로우"
        binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnFollow.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_follow, null)
        followerCnt -= 1
        binding.tvFollowerCount.text = followerCnt.toString()
    }

    private fun setFollowing(){
        binding.btnFollow.text = "팔로잉"
        binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.gray_04))
        binding.btnFollow.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_following, null)
        followerCnt += 1
        binding.tvFollowerCount.text = followerCnt.toString()
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_dj, menu)
        val actionReport = menu.findItem(R.id.action_report)
        val actionBlock = menu.findItem(R.id.action_block)
        if (fromUserId == toUserId){
            // 내 프로필이면 신고/차단 버튼 안뜨게
            actionReport.isVisible = false
            actionBlock.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_block -> {
                dlg.show("이 DJ를 차단 하시겠어요?", "차단")

                dlg.setOnOkClickedListener { content ->
                    when(content){
                        "yes" -> {
                            viewModel.blockUser(fromUserId, null, null, toUserId, "User")
                            showProgress()
                        }
                    }
                }
                true
            }

            R.id.action_report -> {
                dlg.show("이 DJ를 신고 하시겠어요?", "신고")

                dlg.setOnOkClickedListener { content ->
                    when(content){
                        "yes" -> {
                            viewModel.reportObject(fromUserId, null, null, toUserId, "User")
                            showProgress()
                        }
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
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

    override fun onDestroy() {
        super.onDestroy()

        if (isClickFollow){
            val intent = Intent("DJ_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
        }
    }
}