package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class WriteRecordViewModel(application: Application): AndroidViewModel(application) {

    var prevText = ""
    var isPrivate = MutableLiveData<Boolean>()

    var category = MutableLiveData<String>()
    var titleLength = MutableLiveData<Int>()
    var contentsLength = MutableLiveData<Int>()

    init {
        isPrivate.value = false
        titleLength.value = 0
        contentsLength.value = 0
    }

    fun countTitle(et: EditText){
        titleLength.value = et.length()
    }

    fun countContents(et: EditText){
        contentsLength.value = et.length()
    }

    fun changePrivate(){
        isPrivate.value = isPrivate.value == false
    }

    fun setPrevText(et: EditText){
        prevText = et.text.toString()
    }

    fun limitText(et: EditText){
        if (et.lineCount > 2){
            et.setText(prevText)
            et.setSelection(et.length())
        }
    }
}