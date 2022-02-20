package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.data.MyRecord

class RecordDetailViewModel(application: Application): AndroidViewModel(application) {
    lateinit var record: MyRecord


}