package com.infinity.omos.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.infinity.omos.R
import com.infinity.omos.adapters.BlockedListAdapter
import com.infinity.omos.data.user.Profile
import com.infinity.omos.databinding.ActivityBlockingBinding
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.OmosApplication
import com.infinity.omos.viewmodels.BlockedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlockingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockingBinding
    private val viewModel: BlockedViewModel by viewModels()
    private val adapter = BlockedListAdapter()
    private val userId = OmosApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocking)
        initToolBar()

        binding.recyclerView.adapter = adapter
        adapter.setItemClickListener(object : BlockedListAdapter.OnItemClickListener{
            override fun onClick(dj: Profile) {
                showDialog(dj.nickname, userId, dj.userId)
            }
        })

        lifecycleScope.launch {
            search(userId)
        }
    }


    private fun showDialog(name: String, fromUserId: Int, toUserId: Int){
        val dlg = CustomDialog(this)
        dlg.show("${name}님을 차단 해제하시겠어요?", "차단 해제")

        dlg.setOnOkClickedListener { content ->
            when(content){
                "yes" -> {
                    lifecycleScope.launch {
                        viewModel.cancelBlock(fromUserId, toUserId).collectLatest {
                            if (it.state) {
                                Log.d("cancelBlock", "Success")
                            }
                        }
                        search(userId)
                    }
                    Toast.makeText(this, "차단 해제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun search(userId: Int) {
        viewModel.getBlockedUsers(userId).collectLatest {
            adapter.submitList(it)
        }
    }

    private fun initToolBar(){
        binding.toolbar.title = "차단된 계정"
        setSupportActionBar(binding.toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}