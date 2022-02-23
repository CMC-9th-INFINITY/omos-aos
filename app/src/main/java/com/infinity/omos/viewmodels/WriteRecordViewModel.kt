package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel

class WriteRecordViewModel(application: Application): AndroidViewModel(application) {
    var prevText = ""

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