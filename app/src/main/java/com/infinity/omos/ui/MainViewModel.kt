package com.infinity.omos.ui

import androidx.lifecycle.ViewModel
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreManager: DataStoreManager
) : ViewModel() {

    val isExistToken = dataStoreManager.isExistToken()
}