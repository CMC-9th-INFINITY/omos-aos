package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.infinity.omos.adapters.AlbumDetailListAdapter
import com.infinity.omos.databinding.ActivityAlbumBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.viewmodels.AlbumViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class AlbumActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_album)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var albumTitle = intent.getStringExtra("albumTitle")
        var artists = intent.getStringExtra("artists")
        var releaseDate = intent.getStringExtra("releaseDate")
        var albumImageUrl = intent.getStringExtra("albumImageUrl")
        var albumId = intent.getStringExtra("albumId")

        binding.tvAlbumTitle.text = albumTitle
        binding.tvArtistName.text = artists
        binding.tvDate.text = releaseDate
        Glide.with(binding.imgAlbumCover.context)
            .load(albumImageUrl)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_record)
            .into(binding.imgAlbumCover)

        val mAdapter = AlbumDetailListAdapter(this)
        mAdapter.setImageUrl(albumImageUrl)
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // 스크롤 시 앨범 업데이트
        viewModel.setAlbumDetail(albumId!!)
        viewModel.getAlbumDetail().observe(this, Observer { music ->
            music?.let {
                mAdapter.setRecord(it)
                mAdapter.deleteLoading()
                mAdapter.submitList(it)
            }
        })

        // 로딩화면
        viewModel.getStateAlbumDetail().observe(this, Observer { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
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
        })

        initToolBar()
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}