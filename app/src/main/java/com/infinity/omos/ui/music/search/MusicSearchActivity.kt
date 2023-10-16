package com.infinity.omos.ui.music.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.infinity.omos.ui.theme.OmosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmosTheme {
                MusicSearchApp()
            }
        }
    }
}