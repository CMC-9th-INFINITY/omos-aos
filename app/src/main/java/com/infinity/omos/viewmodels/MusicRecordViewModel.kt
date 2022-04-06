package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Record
import com.infinity.omos.data.ResultState
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.RecordRepository
import com.infinity.omos.repository.ReportBlockRepository
import com.infinity.omos.repository.SearchRepository

class MusicRecordViewModel(application: Application): AndroidViewModel(application)  {

    private val repository: SearchRepository = SearchRepository()
    private val recordRepository: RecordRepository = RecordRepository()
    private val reportRepository: ReportBlockRepository = ReportBlockRepository()

    fun setMusicRecord(musicId: String, postId: Int?, size: Int, sortType: String, userId: Int){
        repository.getMusicRecord(musicId, postId, size, sortType, userId)
    }
    fun getMusicRecord():LiveData<List<Record>>{
        return repository.musicRecord
    }
    fun getStateMusicRecord(): LiveData<Constant.ApiState>{
        return repository.stateMusicRecord
    }

    fun saveLike(postId: Int, userId: Int){
        recordRepository.saveLike(postId, userId)
    }

    fun deleteLike(postId: Int, userId: Int){
        recordRepository.deleteLike(postId, userId)
    }

    fun saveScrap(postId: Int, userId: Int){
        recordRepository.saveScrap(postId, userId)
    }

    fun deleteScrap(postId: Int, userId: Int){
        recordRepository.deleteScrap(postId, userId)
    }

    fun reportObject(fromUserId: Int, recordId: Int?, reportReason: String?, toUserId: Int?, type: String){
        reportRepository.reportObject(fromUserId, recordId, reportReason, toUserId, type)
    }
    fun getStateReportRecord(): LiveData<ResultState>{
        return reportRepository.stateReportObject
    }
}