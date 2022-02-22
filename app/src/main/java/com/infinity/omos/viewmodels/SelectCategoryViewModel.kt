package com.infinity.omos.viewmodels

import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.R

class SelectCategoryViewModel(application: Application): AndroidViewModel(application) {

    var state = MutableLiveData<String>()

    init {
        state.value = ""
    }

    fun clickBox(category: TextView){
        if (category.text.toString() == "한 줄 감상"){
            state.value = "한 줄 감상"
        } else if (category.text.toString() == "내 인생의 OST"){
            state.value = "내 인생의 OST"
        } else if (category.text.toString() == "노래 속 나의 이야기"){
            state.value = "노래 속 나의 이야기"
        } else if (category.text.toString() == "나만의 가사해석"){
            state.value = "나만의 가사해석"
        } else if (category.text.toString() == "자유 공간"){
            state.value = "자유 공간"
        }
    }
}