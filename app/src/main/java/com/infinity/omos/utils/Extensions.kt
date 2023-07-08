package com.infinity.omos.utils

import android.app.Activity
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        this@repeatOnStarted.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun Activity.onFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

fun Activity.offFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(true)
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

fun TextView.changeTextColor(word: String) {
    val start = text.indexOf(word)
    val end = start + word.length
    val ssb = SpannableStringBuilder(text)
    ssb.setSpan(
        ForegroundColorSpan(
            ContextCompat.getColor(
                context,
                ColorUtil.getPrimaryColor(context)
            )
        ), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = ssb
}