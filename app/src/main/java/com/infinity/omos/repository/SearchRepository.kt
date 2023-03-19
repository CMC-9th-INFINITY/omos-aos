package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.RecordService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.api.SearchService
import com.infinity.omos.data.*
import com.infinity.omos.etc.Constant
import com.infinity.omos.OmosApplication
import com.infinity.omos.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SearchRepository {

    private val retrofit: Retrofit = RetrofitAPI.getInstnace()
    private val searchApi = retrofit.create(SearchService::class.java)
    private val musicRecordApi = retrofit.create(RecordService::class.java)
    private val onBoardingRepository = OnBoardingRepository()

    var musicRecord = MutableLiveData<List<Record>>()
    var stateMusicRecord = MutableLiveData<Constant.ApiState>()
    fun getMusicRecord(musicId: String, postId: Int?, size: Int, sortType: String, userId: Int){
        stateMusicRecord.value = Constant.ApiState.LOADING
        musicRecordApi.getMusicRecord(musicId, postId, size, sortType, userId).enqueue(object: Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MusicRecordAPI", "Success")
                        musicRecord.postValue(body!!)
                        stateMusicRecord.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MusicRecordAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        getMusicRecord(musicId, postId, size, sortType, userId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MusicRecordAPI", errorBody!!.message)
                        musicRecord.postValue(emptyList())
                        stateMusicRecord.value = Constant.ApiState.ERROR
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

    var artistAlbum = MutableLiveData<List<Album>>()
    var stateArtistAlbum = MutableLiveData<Constant.ApiState>()
    fun getArtistAlbum(artistId: String, limit: Int, offset: Int){
        stateArtistAlbum.value = Constant.ApiState.LOADING
        searchApi.getArtistAlbum(artistId, limit, offset).enqueue(object: Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistAlbumAPI", "Success")
                        artistAlbum.postValue(body!!)
                        stateArtistAlbum.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistAlbumAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        getArtistAlbum(artistId, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistAlbumAPI", errorBody!!.message)
                        stateArtistAlbum.value = Constant.ApiState.ERROR
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

    var artistMusic = MutableLiveData<List<ArtistMusic>>()
    var stateArtistMusic = MutableLiveData<Constant.ApiState>()
    fun getArtistMusic(artistId: String){
        stateArtistMusic.value = Constant.ApiState.LOADING
        searchApi.getArtistMusic(artistId).enqueue(object: Callback<List<ArtistMusic>> {
            override fun onResponse(call: Call<List<ArtistMusic>>, response: Response<List<ArtistMusic>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistMusicAPI", "Success")
                        artistMusic.postValue(body!!)
                        stateArtistMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistMusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        getArtistMusic(artistId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistMusicAPI", errorBody!!.message)
                        stateArtistMusic.value = Constant.ApiState.ERROR
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

    var albumDetail = MutableLiveData<List<Music>>()
    var stateAlbumDetail = MutableLiveData<Constant.ApiState>()
    fun getAlbumDetail(albumId: String){
        stateAlbumDetail.value = Constant.ApiState.LOADING
        searchApi.getAlbumDetail(albumId).enqueue(object: Callback<List<Music>> {
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumDetailAPI", "Success")
                        albumDetail.postValue(body!!)
                        stateAlbumDetail.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AlbumDetailAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        getAlbumDetail(albumId)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AlbumDetailAPI", errorBody!!.message)
                        stateAlbumDetail.value = Constant.ApiState.ERROR
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
        searchApi.getArtist(keyword, limit, offset).enqueue(object: Callback<List<Artists>>{
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
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
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
        searchApi.getMusic(keyword, limit, offset).enqueue(object: Callback<List<Music>>{
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
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
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
        searchApi.getAlbum(keyword, limit, offset).enqueue(object: Callback<List<Album>>{
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
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
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

    var searchMusic = MutableLiveData<List<SearchMusic>>()
    var stateSearchMusic = MutableLiveData<Constant.ApiState>()
    fun getSearchMusic(keyword: String, limit: Int, offset: Int){
        stateSearchMusic.value = Constant.ApiState.LOADING
        searchApi.getSearchMusic(keyword, limit, offset).enqueue(object: Callback<List<SearchMusic>>{
            override fun onResponse(call: Call<List<SearchMusic>>, response: Response<List<SearchMusic>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("SearchMusicAPI", "Success")
                        searchMusic.postValue(body!!)
                        stateSearchMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("SearchMusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(OmosApplication.prefs.getUserToken()!!)
                        getSearchMusic(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("SearchMusicAPI", errorBody!!.message)
                        stateSearchMusic.value = Constant.ApiState.ERROR
                    }

                    else -> {
                        Log.d("SearchMusicAPI", "Code: $code")
                    }
                }
            }

            override fun onFailure(call: Call<List<SearchMusic>>, t: Throwable) {
                Log.d("SearchMusicAPI", t.message.toString())
                t.stackTrace
            }
        })
    }
}