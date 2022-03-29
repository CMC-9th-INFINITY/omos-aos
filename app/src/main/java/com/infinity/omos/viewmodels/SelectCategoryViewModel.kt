package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SelectCategoryViewModel(application: Application): AndroidViewModel(application) {

    var state = MutableLiveData<String>()

    init {
        state.value = ""
    }

    fun clickBox(category: TextView){
        when {
            category.text.toString() == "한 줄 감상" -> {
                state.value = "한 줄 감상"
            }
            category.text.toString() == "내 인생의 OST" -> {
                state.value = "내 인생의 OST"
            }
            category.text.toString() == "노래 속 나의 이야기" -> {
                state.value = "노래 속 나의 이야기"
            }
            category.text.toString() == "나만의 가사해석" -> {
                state.value = "나만의 가사해석"
            }
            category.text.toString() == "자유 공간" -> {
                state.value = "자유 공간"
            }
        }
    }
}