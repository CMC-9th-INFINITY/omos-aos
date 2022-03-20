package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.ResultUpdate
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.data.Update
import com.infinity.omos.repository.MyRecordRepository

class WriteRecordViewModel(application: Application): AndroidViewModel(application) {

    private val repository: MyRecordRepository = MyRecordRepository()

    var prevText = ""
    var isPublic = MutableLiveData<Boolean>()

    var musicTitle = MutableLiveData<String>()
    var artists = MutableLiveData<String>()
    var albumImageUrl = MutableLiveData<String>()
    var createdDate = MutableLiveData<String>()
    var category = MutableLiveData<String>()

    init {
        isPublic.value = true
    }

    fun saveRecord(record: SaveRecord){
        repository.saveRecord(record)
    }
    fun getStateSaveRecord(): LiveData<ResultState>{
        return repository.stateSaveRecord
    }
    fun updateRecord(postId: Int, params: Update){
        repository.updateRecord(postId, params)
    }
    fun getStateUpdateRecord(): LiveData<ResultState>{
        return repository.stateUpdateRecord
    }

    fun changePrivate(){
        isPublic.value = isPublic.value == false
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