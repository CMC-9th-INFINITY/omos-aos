package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.*

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val todayRepository: TodayRepository = TodayRepository()
    private val myRecordRepository: MyRecordRepository = MyRecordRepository()
    private val allRecordsRepository: AllRecordsRepository = AllRecordsRepository()
    private val myDjRepository: MyDjRepository = MyDjRepository()
    private val myPageRepository: MyPageRepository = MyPageRepository()
    private val recordRepository: RecordRepository = RecordRepository()
    private val reportRepository: ReportBlockRepository = ReportBlockRepository()

    // Today
    // 인기있는 레코드
    fun setFamousRecord(){
        todayRepository.getFamousRecord()
    }
    fun getFamousRecord(): LiveData<List<SumRecord>>{
        return todayRepository.famousRecord
    }
    fun getStateFamousRecord(): LiveData<Constant.ApiState>{
        return todayRepository.stateFamousRecord
    }

    // 사랑했던 노래
    fun setMyLoveMusic(userId: Int){
        todayRepository.getMyLoveMusic(userId)
    }
    fun getMyLoveMusic(): LiveData<LovedMusic>{
        return todayRepository.loveMusic
    }
    fun getStateLoveMusic(): LiveData<Constant.ApiState>{
        return todayRepository.stateLoveMusic
    }

    // 오늘의 노래
    fun setTodayMusic(){
        todayRepository.getTodayMusic()
    }
    fun getTodayMusic(): LiveData<Music>{
        return todayRepository.todayMusic
    }
    fun getStateTodayMusic(): LiveData<Constant.ApiState>{
        return todayRepository.stateTodayMusic
    }

    // 추천 DJ
    fun setRecommendDj(){
        todayRepository.getRecommendDj()
    }
    fun getRecommendDj(): LiveData<List<Profile>>{
        return todayRepository.recommendDj
    }
    fun getStateRecommendDj(): LiveData<Constant.ApiState>{
        return todayRepository.stateRecommendDj
    }

    // My 레코드
    fun setMyRecord(userId: Int){
        myRecordRepository.getMyRecord(userId)
    }
    fun getMyRecord(): LiveData<List<SimpleRecord>>{
        return myRecordRepository.myRecord
    }
    fun getStateMyRecord(): LiveData<Constant.ApiState>{
        return myRecordRepository.stateMyRecord
    }

    // 전체 레코드
    fun setAllRecords(userId: Int){
        allRecordsRepository.setAllRecords(userId)
    }
    fun getAllRecords(): LiveData<Category>{
        return allRecordsRepository.allRecords
    }
    fun getStateAllRecords(): LiveData<Constant.ApiState>{
        return allRecordsRepository.stateAllRecords
    }

    // My DJ
    // DJ 리스트
    fun setMyDj(userId: Int){
        myDjRepository.getMyDj(userId)
    }
    fun getMyDj(): LiveData<List<Profile>>{
        return myDjRepository.myDj
    }
    fun getStateMyDj(): LiveData<Constant.ApiState>{
        return myDjRepository.stateMyDj
    }

    // 선택된 DJ 레코드
    private val myDjRecord = myDjRepository.myDjRecord
    fun setMyDjRecord(fromUserId: Int, toUserId: Int){
        myDjRepository.getMyDjRecord(fromUserId, toUserId)
    }
    fun getMyDjRecord(): LiveData<List<Record>>{
        return myDjRecord
    }
    fun getStateDjRecord(): LiveData<Constant.ApiState>{
        return myDjRepository.stateMyDjRecord
    }

    // 모든 DJ 레코드
    fun setDjAllRecords(userId: Int, postId: Int?, size: Int){
        myDjRepository.getMyDjAllRecords(userId, postId, size)
    }
    fun getDjAllRecords(): LiveData<List<Record>>{
        return myDjRepository.myDjAllRecords
    }
    fun getStateDjAllRecords(): LiveData<Constant.ApiState>{
        return myDjRepository.stateMyDjAllRecords
    }

    // MY 페이지
    private val repository: MyDjRepository = MyDjRepository()

    // 내 프로필
    fun setDjProfile(fromUserId: Int, toUserId: Int){
        repository.getDjProfile(fromUserId, toUserId)
    }
    fun getDjProfile(): LiveData<DjProfile>{
        return repository.djProfile
    }

    // 스크랩, 공감 레코드
    fun setMyPageData(userId: Int){
        myPageRepository.getMyPageData(userId)
    }
    fun getMyPageData(): LiveData<MyPage>{
        return myPageRepository.myPageData
    }
    fun getStateMyPageData(): LiveData<Constant.ApiState>{
        return myPageRepository.stateMyPageData
    }

    // 스크랩, 공감 클릭
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