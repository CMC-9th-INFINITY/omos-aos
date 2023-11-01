package com.infinity.omos.ui.record.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.infinity.omos.ui.theme.OmosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmosTheme {
                DetailRecordScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}