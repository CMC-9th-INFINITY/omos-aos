package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.*
import com.infinity.omos.data.*
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 *  수정 필요
 */
class Repository {

    /**
     *  추후 삭제 요망
     */
    private val testRetrofit: Retrofit = RetrofitAPI.getMovieInstnace()
    private val api = testRetrofit.create(MyRecordService::class.java)
    private val myDjApi = testRetrofit.create(MyDjService::class.java)

    enum class LoginApiState{LOADING, ERROR, DONE}
    enum class ApiState{LOADING, ERROR, DONE, TOKEN}

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()

    // 온보딩
    private val loginApi = retrofit.create(LoginService::class.java)
    private val signUpApi = retrofit.create(SignUpService::class.java)
    private val snsLoginApi = retrofit.create(SnsLoginService::class.java)
    private val snsSignUpApi = retrofit.create(SnsSignUpService::class.java)
    private val checkDupApi = retrofit.create(DupEmailService::class.java)
    private val reissueApi = retrofit.create(ReissueService::class.java)
    var _stateLogin = MutableLiveData<LoginApiState>()
    var _stateSignUp = MutableLiveData<LoginApiState>()
    var _stateSnsLogin = MutableLiveData<LoginApiState>()
    var _stateSnsSignUp = MutableLiveData<LoginApiState>()
    var _stateDupEmail = MutableLiveData<Boolean>()

    // MainActivity
    var _stateToken = MutableLiveData<ApiState>()

    // My 레코드
    var _myRecord = MutableLiveData<List<MyRecord>>()
    var _stateMyRecord = MutableLiveData<ApiState>()

    // 전체 레코드
    private val allRecordsApi = retrofit.create(AllRecordsService::class.java)
    private val categoryApi = retrofit.create(CategoryService::class.java)
    var _allRecords = MutableLiveData<Category?>()
    var _category = MutableLiveData<List<DetailCategory>?>()
    var _stateAllRecords = MutableLiveData<ApiState>()
    var _stateCategory = MutableLiveData<ApiState>()

    // My DJ
    var _djRecord = MutableLiveData<List<MyRecord>>()

    // 검색
    private val musicApi = retrofit.create(SearchMusicService::class.java)
    private val albumApi = retrofit.create(SearchAlbumService::class.java)
    private val artistApi = retrofit.create(SearchArtistService::class.java)
    private val albumDetailApi = retrofit.create(AlbumDetailService::class.java)
    var _album = MutableLiveData<List<Album>?>()
    var _music = MutableLiveData<List<Music>?>()
    var _artist = MutableLiveData<List<Artists>?>()
    var _albumDetail = MutableLiveData<List<Music>?>()
    var _stateAlbum = MutableLiveData<ApiState>()
    var _stateMusic = MutableLiveData<ApiState>()
    var _stateArtist = MutableLiveData<ApiState>()
    var _stateAlbumDetail = MutableLiveData<ApiState>()

    fun getAlbumDetail(albumId: String){
        _stateAlbumDetail.value = ApiState.LOADING
        albumDetailApi.getAlbumDetail(albumId).enqueue(object: Callback<List<Music>>{
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumDetailAPI Response", response.body().toString())
                        _albumDetail.value = body
                        _stateAlbumDetail.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getAlbumDetail(albumId)
                    }

                    else -> {
                        Log.d("Code", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Music>>, t: Throwable) {
                Log.d("AlbumDetailAPI Failure", t.message.toString())
                _stateAlbumDetail.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getArtist(keyword: String, limit: Int, offset: Int){
        _stateArtist.value = ApiState.LOADING
        artistApi.getArtist(keyword, limit, offset).enqueue(object: Callback<List<Artists>>{
            override fun onResponse(call: Call<List<Artists>>, response: Response<List<Artists>>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistAPI Response", response.body().toString())
                        _artist.value = body
                        _stateArtist.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getArtist(keyword, limit, offset)
                    }

                    else -> {
                        Log.d("Code", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Artists>>, t: Throwable) {
                Log.d("ArtistAPI Failure", t.message.toString())
                _stateArtist.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getMusic(keyword: String, limit: Int, offset: Int){
        _stateMusic.value = ApiState.LOADING
        musicApi.getMusic(keyword, limit, offset).enqueue(object: Callback<List<Music>>{
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MusicAPI Response", response.body().toString())
                        _music.value = body
                        _stateMusic.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMusic(keyword, limit, offset)
                    }

                    else -> {
                        Log.d("Code", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Music>>, t: Throwable) {
                Log.d("MusicAPI Failure", t.message.toString())
                _stateMusic.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getAlbum(keyword: String, limit: Int, offset: Int){
        _stateAlbum.value = ApiState.LOADING
        albumApi.getAlbum(keyword, limit, offset).enqueue(object: Callback<List<Album>>{
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumAPI Response", response.body().toString())
                        _album.value = body
                        _stateAlbum.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getAlbum(keyword, limit, offset)
                    }

                    else -> {
                        Log.d("Code", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                Log.d("AlbumAPI Failure", t.message.toString())
                _stateAlbum.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun setCategory(category: String, page: Int, size: Int, sort: String?, userId: Int){
        _stateCategory.value = ApiState.LOADING
        categoryApi.setCategory(category, page, size, sort, userId).enqueue(object: Callback<List<DetailCategory>?>{
            override fun onResponse(
                call: Call<List<DetailCategory>?>,
                response: Response<List<DetailCategory>?>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("CategoryAPI Response", response.body().toString())
                    _category.value = body
                    _stateCategory.value = ApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("CategoryAPI Error", errorBody!!.message)

                    if (errorBody.message == "Refresh Token이 유효하지 않습니다."){
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        _stateCategory.value = ApiState.TOKEN
                    }
                }
            }

            override fun onFailure(call: Call<List<DetailCategory>?>, t: Throwable) {
                Log.d("CategoryAPI Failure", t.message.toString())
                _stateCategory.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun setAllRecords(){
        _stateAllRecords.value = ApiState.LOADING
        allRecordsApi.setAllRecords().enqueue(object: Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AllRecordsAPI Response", response.body().toString())
                        _allRecords.value = body
                        _stateAllRecords.value = ApiState.DONE
                    }

                    401 -> {
                        Log.d("Unauthorized", "reissue")
                        getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        _stateAllRecords.value = ApiState.TOKEN
                    }

                    else -> {
                        Log.d("Repository", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("AllRecordsAPI Failure", t.message.toString())
                _stateAllRecords.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getUserToken(userInfo: UserToken){
        reissueApi.getToken(userInfo).enqueue(object: Callback<UserToken>{
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()

                when(val code = response.code()){
                    in 200..300 -> {
                        GlobalApplication.prefs.setUserToken(
                            body?.accessToken,
                            body?.refreshToken,
                            body?.userId!!)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("Reissue API", errorBody!!.message)
                        if (errorBody.message == "토큰의 유저 정보가 일치하지 않습니다."){
                            _stateToken.value = ApiState.ERROR
                        }
                    }

                    else -> {
                        Log.d("Repository", code.toString())
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                t.stackTrace
            }
        })
    }

    fun getTestData(page: Int): LiveData<List<MyRecord>>{
        val data = MutableLiveData<List<MyRecord>>()

        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                data.value = response.body()?.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }

    fun getMyRecordData(page: Int){
        _stateMyRecord.value = ApiState.LOADING

        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                _myRecord.value = response.body()?.myRecordList
                _stateMyRecord.value = ApiState.DONE
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                _stateMyRecord.value = ApiState.ERROR
                t.stackTrace
            }
        })
    }

    fun getMyDjRecordData(page: Int){
        api.getResultGetMyRecord(page).enqueue(object: Callback<ResultGetMyRecord> {
            override fun onResponse(
                call: Call<ResultGetMyRecord>,
                response: Response<ResultGetMyRecord>
            ) {
                _djRecord.value = response.body()?.myRecordList
            }

            override fun onFailure(call: Call<ResultGetMyRecord>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getMyDjData(page: Int): LiveData<List<MyDj>>{
        val data = MutableLiveData<List<MyDj>>()

        myDjApi.getResultMyDj(page).enqueue(object: Callback<ResultGetMyDj> {
            override fun onResponse(
                call: Call<ResultGetMyDj>,
                response: Response<ResultGetMyDj>
            ) {
                data.value = response.body()?.myDjList
            }

            override fun onFailure(call: Call<ResultGetMyDj>, t: Throwable) {
                Log.d("Repository", t.message.toString())
                t.stackTrace
            }
        })

        return data
    }

    fun checkLogin(userLogin: UserLogin){
        loginApi.getResultLogin(userLogin).enqueue(object: Callback<UserToken> {
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                Log.d("LoginAPI", response.body().toString())
                if (response.body() != null){
                    _stateLogin.value = LoginApiState.DONE
                    GlobalApplication.prefs.setUserToken(
                        response.body()?.accessToken,
                        response.body()?.refreshToken,
                        response.body()?.userId!!)
                } else{
                    _stateLogin.value = LoginApiState.ERROR
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("LoginAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun signUp(){

    }

    fun checkSnsLogin(id: UserSnsLogin){
        snsLoginApi.getResultSnsLogin(id).enqueue(object: Callback<UserToken> {
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("SnsLoginAPI Response", response.body().toString())
                    GlobalApplication.prefs.setUserToken(
                        body.accessToken,
                        body.refreshToken,
                        body.userId!!)
                    _stateSnsLogin.value = LoginApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("SnsLoginAPI Error", errorBody!!.message)

                    if (errorBody.message == "해당하는 유저가 존재하지 않습니다"){
                        _stateSnsLogin.value = LoginApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("SnsLoginAPI Failure", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun signUp(userInfo: UserSignUp){
        signUpApi.getResultSignUp(userInfo).enqueue(object: Callback<ResultState>{
            override fun onResponse(call: Call<ResultState>, response: Response<ResultState>) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("signUpAPI Response", response.body().toString())
                    if(response.body()?.state == true){
                        _stateSignUp.value = LoginApiState.DONE
                    } else{
                        _stateSignUp.value = LoginApiState.ERROR
                    }
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("signUpAPI Error", errorBody!!.message)

                    if (errorBody.message == "이미 있는 닉네임입니다"){
                        _stateSignUp.value = LoginApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun snsSignUp(userInfo: UserSnsSignUp){
        snsSignUpApi.getResultSnsSignUp(userInfo).enqueue(object: Callback<UserToken>{
            override fun onResponse(
                call: Call<UserToken>,
                response: Response<UserToken>
            ) {
                val body = response.body()
                if (body != null && response.isSuccessful) {
                    Log.d("signUpAPI Response", response.body().toString())
                    GlobalApplication.prefs.setUserToken(
                        response.body()?.accessToken,
                        response.body()?.refreshToken,
                        response.body()?.userId!!)
                    _stateSnsSignUp.value = LoginApiState.DONE
                } else {
                    val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                    Log.d("signUpAPI Error", errorBody!!.message)

                    if (errorBody.message == "이미 있는 닉네임입니다"){
                        _stateSnsSignUp.value = LoginApiState.ERROR
                    }
                }
            }

            override fun onFailure(call: Call<UserToken>, t: Throwable) {
                Log.d("signUpAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun checkDupEmail(email: String){
        checkDupApi.checkDupEmail(email).enqueue(object: Callback<ResultState>{
            override fun onResponse(
                call: Call<ResultState>,
                response: Response<ResultState>
            ) {
                Log.d("checkDupEmailAPI", response.body()?.state.toString())
                _stateDupEmail.value = response.body()?.state
            }

            override fun onFailure(call: Call<ResultState>, t: Throwable) {
                Log.d("checkDupEmailAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}