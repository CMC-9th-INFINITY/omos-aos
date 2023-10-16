package com.infinity.omos.utils

import android.content.Context

object ColorUtil {

    fun getPrimaryColor(context: Context): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimary))
        val colorPrimary = typedArray.run {
            val color = getColor(0, 0)
            recycle()
            color
        }

        return colorPrimary
    }
}