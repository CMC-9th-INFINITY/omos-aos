package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.DjProfile
import com.infinity.omos.data.Profile
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyDjRepository

class DjViewModel(application: Application): AndroidViewModel(application) {

    private val repository: MyDjRepository = MyDjRepository()

    fun setDjProfile(fromUserId: Int, toUserId: Int){
        repository.getDjProfile(fromUserId, toUserId)
    }
    fun getDjProfile(): LiveData<DjProfile>{
        return repository.djProfile
    }

    fun saveFollow(fromUserId: Int, toUserId: Int){
        repository.saveFollow(fromUserId, toUserId)
    }

    fun deleteFollow(fromUserId: Int, toUserId: Int){
        repository.deleteFollow(fromUserId, toUserId)
    }

    fun setDjRecord(fromUserId: Int, toUserId: Int){
        repository.getMyDjRecord(fromUserId, toUserId)
    }
    fun getDjRecord(): LiveData<List<Record>>{
        return repository.myDjRecord
    }
    fun getStateDjRecord(): LiveData<Constant.ApiState>{
        return repository.stateMyDjRecord
    }

    var follow = MutableLiveData<String>()

    fun changeFollow(){
        if (follow.value == "팔로우"){
            follow.value = "팔로잉"
        } else{
            follow.value = "팔로우"
        }
    }
}