package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.*
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SearchRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val onBoardingRepository = OnBoardingRepository()

    private val musicApi = retrofit.create(SearchMusicService::class.java)
    private val albumApi = retrofit.create(SearchAlbumService::class.java)
    private val artistApi = retrofit.create(SearchArtistService::class.java)
    private val albumDetailApi = retrofit.create(AlbumDetailService::class.java)
    private val artistMusicApi = retrofit.create(ArtistMusicService::class.java)
    private val artistAlbumApi = retrofit.create(ArtistAlbumService::class.java)
    private val musicRecordApi = retrofit.create(MusicRecordService::class.java)
    var _albumDetail = MutableLiveData<List<Music>?>()
    var _artistMusic = MutableLiveData<List<ArtistMusic>?>()
    var _artistAlbum = MutableLiveData<List<Album>?>()
    var _musicRecord = MutableLiveData<List<Record>?>()


    var _stateAlbumDetail = MutableLiveData<Constant.ApiState>()
    var _stateArtistMusic = MutableLiveData<Constant.ApiState>()
    var _stateArtistAlbum = MutableLiveData<Constant.ApiState>()
    var _stateMusicRecord = MutableLiveData<Constant.ApiState>()

    fun getMusicRecord(musicId: String, postId: Int?, size: Int, userId: Int){
        _stateMusicRecord.value = Constant.ApiState.LOADING
        musicRecordApi.getMusicRecord(musicId, postId, size, userId).enqueue(object: Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MusicRecordAPI", "Success")
                        _musicRecord.value = body
                        _stateMusicRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MusicRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMusicRecord(musicId, postId, size, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MusicRecordAPI", errorBody!!.message)
                        _musicRecord.value = emptyList()
                        _stateMusicRecord.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MusicRecordAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d("MusicRecordAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getArtistAlbum(artistId: String, limit: Int, offset: Int){
        _stateArtistAlbum.value = Constant.ApiState.LOADING
        artistAlbumApi.getArtistAlbum(artistId, limit, offset).enqueue(object: Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistAlbumAPI", "Success")
                        _artistAlbum.value = body
                        _stateArtistAlbum.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistAlbumAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getArtistAlbum(artistId, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistAlbumAPI", errorBody!!.message)
                        _stateArtistAlbum.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ArtistAlbumAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                Log.d("ArtistAlbumAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getArtistMusic(artistId: String){
        _stateArtistMusic.value = Constant.ApiState.LOADING
        artistMusicApi.getArtistMusic(artistId).enqueue(object: Callback<List<ArtistMusic>> {
            override fun onResponse(call: Call<List<ArtistMusic>>, response: Response<List<ArtistMusic>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistMusicAPI", "Success")
                        _artistMusic.value = body
                        _stateArtistMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistMusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getArtistMusic(artistId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistMusicAPI", errorBody!!.message)
                        _stateArtistMusic.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ArtistMusicAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<ArtistMusic>>, t: Throwable) {
                Log.d("ArtistMusicAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    fun getAlbumDetail(albumId: String){
        _stateAlbumDetail.value = Constant.ApiState.LOADING
        albumDetailApi.getAlbumDetail(albumId).enqueue(object: Callback<List<Music>> {
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumDetailAPI", "Success")
                        _albumDetail.value = body
                        _stateAlbumDetail.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AlbumDetailAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getAlbumDetail(albumId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AlbumDetailAPI", errorBody!!.message)
                        _stateAlbumDetail.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("AlbumDetailAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Music>>, t: Throwable) {
                Log.d("AlbumDetailAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var artist = MutableLiveData<List<Artists>>()
    var stateArtist = MutableLiveData<Constant.ApiState>()
    fun getArtist(keyword: String, limit: Int, offset: Int){
        stateArtist.value = Constant.ApiState.LOADING
        artistApi.getArtist(keyword, limit, offset).enqueue(object: Callback<List<Artists>>{
            override fun onResponse(call: Call<List<Artists>>, response: Response<List<Artists>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistAPI", "Success")
                        artist.postValue(body!!)
                        stateArtist.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getArtist(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistAPI", errorBody!!.message)
                        stateArtist.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("ArtistAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Artists>>, t: Throwable) {
                Log.d("ArtistAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var music = MutableLiveData<List<Music>>()
    var stateMusic = MutableLiveData<Constant.ApiState>()
    fun getMusic(keyword: String, limit: Int, offset: Int){
        stateMusic.value = Constant.ApiState.LOADING
        musicApi.getMusic(keyword, limit, offset).enqueue(object: Callback<List<Music>>{
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MusicAPI", "Success")
                        music.postValue(body!!)
                        stateMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMusic(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MusicAPI", errorBody!!.message)
                        stateMusic.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("MusicAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Music>>, t: Throwable) {
                Log.d("MusicAPI", t.message.toString())
                t.stackTrace
            }
        })
    }

    var album = MutableLiveData<List<Album>>()
    var stateAlbum = MutableLiveData<Constant.ApiState>()
    fun getAlbum(keyword: String, limit: Int, offset: Int){
        stateAlbum.value = Constant.ApiState.LOADING
        albumApi.getAlbum(keyword, limit, offset).enqueue(object: Callback<List<Album>>{
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumAPI", "Success")
                        album.postValue(body!!)
                        stateAlbum.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AlbumAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getAlbum(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AlbumAPI", errorBody!!.message)
                        stateAlbum.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("AlbumAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                Log.d("AlbumAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}