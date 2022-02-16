package com.infinity.omos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_record_detail.*

class RecordDetailActivity : AppCompatActivity() {

    lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        path = intent.getStringExtra("img_path")!!
        val baseUrl = "https://image.tmdb.org/t/p/w500/"
        Glide.with(this).load(baseUrl+path).error(R.drawable.ic_launcher_background).into(img_album_cover)
    }
}