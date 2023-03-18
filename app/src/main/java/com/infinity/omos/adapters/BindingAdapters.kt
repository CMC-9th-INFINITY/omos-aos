package com.infinity.omos.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.infinity.omos.ui.onboarding.OmosFieldView
import com.infinity.omos.ui.onboarding.ErrorField

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
    view.setShowErrorMsg(error)
}