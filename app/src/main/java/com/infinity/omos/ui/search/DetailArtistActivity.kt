package com.infinity.omos.ui.search

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity.omos.R
import com.infinity.omos.adapters.ArtistViewPagerAdapter
import com.infinity.omos.databinding.ActivityArtistBinding
import com.infinity.omos.viewmodels.ArtistViewModel

class DetailArtistActivity : AppCompatActivity() {

    private val viewModel: ArtistViewModel by viewModels()
    private lateinit var binding: ActivityArtistBinding

    private var artistId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        artistId = intent.getStringExtra("artistId")!!
        var name = intent.getStringExtra("artistName")
        var url = intent.getStringExtra("artistImageUrl")
        var genres = intent.getStringExtra("artistGenres")

        initToolBar()
        initTabLayout()

        binding.tvArtistName.text = name
        binding.tvArtistGenres.text = genres
        Glide.with(binding.imgProfile.context)
            .load(url)
            .error(R.drawable.ic_record)
            .fallback(R.drawable.ic_record)
            .into(binding.imgProfile)
    }

    private fun initTabLayout(){
        val viewpagerFragmentAdapter = ArtistViewPagerAdapter(this, artistId)
        binding.viewPager.adapter = viewpagerFragmentAdapter
        binding.viewPager.isUserInputEnabled = false
        val tabTitles = listOf("노래", "앨범")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initToolBar(){
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar) // 툴바 사용

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