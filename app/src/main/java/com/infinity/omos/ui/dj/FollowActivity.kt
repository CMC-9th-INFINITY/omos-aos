package com.infinity.omos.ui.dj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infinity.omos.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)
    }
}