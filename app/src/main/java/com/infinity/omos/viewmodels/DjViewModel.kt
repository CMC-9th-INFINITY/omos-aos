package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.DjProfile
import com.infinity.omos.data.Profile
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


    var follow = MutableLiveData<String>()

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
}