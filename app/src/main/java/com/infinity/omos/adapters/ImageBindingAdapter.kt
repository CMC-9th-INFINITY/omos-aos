package com.infinity.omos.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
            .error(R.drawable.ic_record)
            .fallback(R.drawable.ic_record)
            .into(imageView)
    }

    @BindingAdapter("profileImageUrl")
    @JvmStatic
    fun loadProfileImage(imageView: ImageView, url: String?){
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.ic_profile)
            .fallback(R.drawable.ic_profile)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    // recordImageUrl 이 null 일 때, 빈 이미지 보여주기 위함
    @BindingAdapter("categoryImageUrl")
    @JvmStatic
    fun loadCategoryImage(imageView: ImageView, url: String?){
        Glide.with(imageView.context)
            .load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }
}