package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.data.SaveRecord

class RecordDetailViewModel(application: Application): AndroidViewModel(application) {
    lateinit var record: SaveRecord


}