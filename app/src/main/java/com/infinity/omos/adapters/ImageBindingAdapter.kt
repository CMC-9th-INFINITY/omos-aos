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
    fun loadImage(imageView: ImageView, url: String){
        val baseUrl = "https://image.tmdb.org/t/p/w500/"
        Glide.with(imageView.context).load(baseUrl+url).error(R.drawable.ic_launcher_background).into(imageView)
    }
}