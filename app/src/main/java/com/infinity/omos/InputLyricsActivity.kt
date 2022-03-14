package com.infinity.omos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.infinity.omos.data.Music
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.databinding.ActivityInputLyricsBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.GlobalApplication
import kotlinx.android.synthetic.main.activity_register_nick.*

class InputLyricsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputLyricsBinding

    private var category = ""
    private var musicId = ""
    private var musicTitle = ""
    private var artists = ""
    private var albumImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_lyrics)
        binding.lifecycleOwner = this

        initToolBar()

        category = intent.getStringExtra("category")!!
        musicId = intent.getStringExtra("musicId")!!
        musicTitle = intent.getStringExtra("musicTitle")!!
        artists = intent.getStringExtra("artists")!!
        albumImageUrl = intent.getStringExtra("albumImageUrl")!!

        binding.tvMusicTitle.text = musicTitle
        binding.tvArtist.text = artists
        Glide.with(binding.imgAlbumCover.context)
            .load(albumImageUrl)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_record)
            .into(binding.imgAlbumCover)

        binding.etLyrics.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvContentsCount.text = binding.etLyrics.length().toString()
            }
        })
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_next, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_next -> {
                if (binding.etLyrics.text.isEmpty()){
                    Toast.makeText(this, "가사를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else{
                    val intent = Intent(this, WriteLyricsActivity::class.java)
                    intent.putExtra("musicId", musicId)
                    intent.putExtra("musicTitle", musicTitle)
                    intent.putExtra("artists", artists)
                    intent.putExtra("albumImageUrl", albumImageUrl)
                    intent.putExtra("category", category)
                    intent.putExtra("lyrics", binding.etLyrics.text.toString())
                    startActivity(intent)
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
}