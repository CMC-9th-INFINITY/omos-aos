package com.infinity.omos.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager

class Height {

    companion object{

        fun Context.screenHeight(): Int {
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.height() - insets.bottom - insets.top
            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.heightPixels
            }
        }

        fun Context.statusBarHeight(): Int {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

            return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
            else 0
        }

        fun Context.navigationHeight(): Int {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

            return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
            else 0
        }
    }
}