package com.infinity.omos.adapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isGone")
fun bindGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}