package com.infinity.omos.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.api.*
import com.infinity.omos.data.Album
import com.infinity.omos.data.ArtistMusic
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Music
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
    var _album = MutableLiveData<List<Album>?>()
    var _music = MutableLiveData<List<Music>?>()
    var _artist = MutableLiveData<List<Artists>?>()
    var _albumDetail = MutableLiveData<List<Music>?>()
    var _artistMusic = MutableLiveData<List<ArtistMusic>?>()
    var _artistAlbum = MutableLiveData<List<Album>?>()
    var _stateAlbum = MutableLiveData<Constant.ApiState>()
    var _stateMusic = MutableLiveData<Constant.ApiState>()
    var _stateArtist = MutableLiveData<Constant.ApiState>()
    var _stateAlbumDetail = MutableLiveData<Constant.ApiState>()
    var _stateArtistMusic = MutableLiveData<Constant.ApiState>()
    var _stateArtistAlbum = MutableLiveData<Constant.ApiState>()

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

    fun getArtist(keyword: String, limit: Int, offset: Int){
        _stateArtist.value = Constant.ApiState.LOADING
        artistApi.getArtist(keyword, limit, offset).enqueue(object: Callback<List<Artists>>{
            override fun onResponse(call: Call<List<Artists>>, response: Response<List<Artists>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("ArtistAPI", "Success")
                        _artist.value = body
                        _stateArtist.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("ArtistAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getArtist(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("ArtistAPI", errorBody!!.message)
                        _stateArtist.value = Constant.ApiState.ERROR
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

    fun getMusic(keyword: String, limit: Int, offset: Int){
        _stateMusic.value = Constant.ApiState.LOADING
        musicApi.getMusic(keyword, limit, offset).enqueue(object: Callback<List<Music>>{
            override fun onResponse(call: Call<List<Music>>, response: Response<List<Music>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("MusicAPI", "Success")
                        _music.value = body
                        _stateMusic.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("MusicAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getMusic(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("MusicAPI", errorBody!!.message)
                        _stateMusic.value = Constant.ApiState.ERROR
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

    fun getAlbum(keyword: String, limit: Int, offset: Int){
        _stateAlbum.value = Constant.ApiState.LOADING
        albumApi.getAlbum(keyword, limit, offset).enqueue(object: Callback<List<Album>>{
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                val body = response.body()
                when(val code = response.code()){
                    in 200..300 -> {
                        Log.d("AlbumAPI", "Success")
                        _album.value = body
                        _stateAlbum.value = Constant.ApiState.DONE
                    }

                    401 -> {
                        Log.d("AlbumAPI", "Unauthorized")
                        onBoardingRepository.getUserToken(GlobalApplication.prefs.getUserToken()!!)
                        getAlbum(keyword, limit, offset)
                    }

                    500 -> {
                        val errorBody = NetworkUtil.getErrorResponse(response.errorBody()!!)
                        Log.d("AlbumAPI", errorBody!!.message)
                        _stateAlbum.value = Constant.ApiState.ERROR
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