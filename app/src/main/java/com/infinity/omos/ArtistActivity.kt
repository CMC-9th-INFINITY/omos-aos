package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.infinity.omos.databinding.ActivityAlbumBinding
import com.infinity.omos.databinding.ActivityArtistBinding
import com.infinity.omos.viewmodels.ArtistViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class ArtistActivity : AppCompatActivity() {

    private val viewModel: ArtistViewModel by viewModels()
    private lateinit var binding: ActivityArtistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        var name = intent.getStringExtra("artistName")
        var url = intent.getStringExtra("artistImageUrl")
        var genres = intent.getStringExtra("artistGenres")

        binding.tvArtistName.text = name
        binding.tvArtistGenres.text = genres
        Glide.with(binding.imgProfile.context).load(url).error(R.drawable.ic_launcher_background).into(binding.imgProfile)
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