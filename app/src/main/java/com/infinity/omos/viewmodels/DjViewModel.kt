package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.Repository

class DjViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    var follow = MutableLiveData<String>()

    val djRecord = repository.getMyRecordData(1)

    init {
        follow.value = "팔로우"
    }

    fun changeFollow(){
        if (follow.value == "팔로우"){
            follow.value = "팔로잉"
        } else{
            follow.value = "팔로우"
        }
    }

    // xml 연결 (djRecordListData)
    fun getDjRecordData(): LiveData<List<MyRecord>> {
        return djRecord
    }
}