package com.infinity.omos.utils

import android.content.Context

class Height {
    companion object{
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