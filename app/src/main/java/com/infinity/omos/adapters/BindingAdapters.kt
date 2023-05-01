package com.infinity.omos.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.infinity.omos.ui.view.OmosFieldView
import com.infinity.omos.ui.onboarding.error.ErrorField

@BindingAdapter("isGone")
fun bindGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("showErrorMsg")
fun bindErrorMsg(view: OmosFieldView, error: ErrorField) {
    view.setShowErrorMsg(error.state, error.msg)
}