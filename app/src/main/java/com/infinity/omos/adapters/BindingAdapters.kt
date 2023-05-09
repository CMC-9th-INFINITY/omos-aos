package com.infinity.omos.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.infinity.omos.R
import com.infinity.omos.ui.view.OmosFieldView
import com.infinity.omos.ui.onboarding.error.ErrorField
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("isGone")
fun bindGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .error(R.drawable.ic_record)
            .fallback(R.drawable.ic_record)
            .into(view)
    }
}