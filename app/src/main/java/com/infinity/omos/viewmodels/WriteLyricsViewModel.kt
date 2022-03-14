package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.repository.MyRecordRepository

class WriteLyricsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: MyRecordRepository = MyRecordRepository()
    fun getStateSaveRecord(): LiveData<ResultState> {
        return repository.stateSaveRecord
    }

    var prevText = ""
    var isPrivate = MutableLiveData<Boolean>()

    var musicTitle = MutableLiveData<String>()
    var artists = MutableLiveData<String>()
    var albumImageUrl = MutableLiveData<String>()
    var createdDate = MutableLiveData<String>()
    var category = MutableLiveData<String>()

    init {
        isPrivate.value = false
    }

    fun saveRecord(record: SaveRecord){
        repository.saveRecord(record)
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