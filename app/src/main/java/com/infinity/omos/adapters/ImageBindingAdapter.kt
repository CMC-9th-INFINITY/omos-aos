package com.infinity.omos.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.infinity.omos.R

/**
 *  수정 필요
 */
object ImageBindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String?){
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_record)
            .into(imageView)
    }
}