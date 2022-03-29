package com.infinity.omos

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                }
            }
        }

        viewModel.getStateSignOut().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.DONE -> {
                        dlg.dismissProgress()
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

                        finish()
                        Toast.makeText(this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    Constant.ApiState.ERROR -> {
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
        if (fromUserId == toUserId){
            // 내 프로필이면 신고버튼 안뜨게
            actionReport.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_report -> {
                dlg = CustomDialog(this)
                dlg.show("이 DJ를 신고하시겠어요?", "신고")

                dlg.setOnOkClickedListener { content ->
                    when(content){
                        "yes" -> {
                            Log.d("SignOutAPI", toUserId.toString())
                            viewModel.signOut(toUserId)
                            dlg.showProgress()
                        }
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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