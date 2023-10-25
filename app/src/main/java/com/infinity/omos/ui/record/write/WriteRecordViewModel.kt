package com.infinity.omos.ui.record.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.data.record.SaveRecordRequest
import com.infinity.omos.repository.music.MusicRepository
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.ui.record.Category
import com.infinity.omos.ui.record.RecordForm
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WriteRecordViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val musicRepository: MusicRepository,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private val _music = MutableStateFlow(
        MusicModel("", "", "", "", "", "")
    )
    val music = _music.asStateFlow()

    val category = MutableStateFlow(Category.A_LINE)
    val recordForm = MutableStateFlow(RecordForm.WRITE)

    val title = MutableStateFlow("")
    val imageUri = MutableStateFlow<Uri?>(null)
    val isPublic = MutableStateFlow(true)
    val lyrics = MutableStateFlow("")
    private val contents = MutableStateFlow("")

    fun setCategory(newCategory: Category) {
        category.value = newCategory
    }

    fun setRecordForm(newRecordForm: RecordForm) {
        recordForm.value = newRecordForm
    }

    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    fun setImageUri(uri: Uri) {
        imageUri.value = uri
    }

    fun setLyrics(newLyrics: String) {
        lyrics.value = newLyrics
    }

    fun changeLockState() {
        isPublic.value = !isPublic.value
    }

    fun setContents(newContents: String) {
        contents.value = newContents
    }

    fun fetchMusic(musicId: String) {
        viewModelScope.launch {
            musicRepository.getMusic(musicId).mapCatching { it.toPresentation() }
                .onSuccess { _music.value = it }
                .onFailure { Timber.d("Error") }
        }
    }

    fun saveRecord() {
        val record = SaveRecordRequest(
            category = category.value.name,
            isPublic = isPublic.value,
            musicId = music.value.musicId,
            recordContents = contents.value,
            recordImageUrl = "",
            recordTitle = title.value,
            userId = userId
        )
        viewModelScope.launch {
            recordRepository.saveRecord(record)
        }
    }
}