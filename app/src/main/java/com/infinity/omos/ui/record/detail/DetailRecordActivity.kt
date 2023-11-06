package com.infinity.omos.ui.record.detail

import android.content.Context
import android.content.Intent
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

fun moveDetailRecord(context: Context, recordId: Int) {
    val intent = Intent(context, DetailRecordActivity::class.java)
    intent.putExtra("recordId", recordId)
    context.startActivity(intent)
}