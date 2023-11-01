package com.infinity.omos.ui.record.write

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.data.record.RecordCategory
import com.infinity.omos.data.record.SaveRecordRequest
import com.infinity.omos.repository.music.MusicRepository
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.ui.record.RecordForm
import com.infinity.omos.utils.AWSConnector
import com.infinity.omos.utils.DataStoreManager
import com.infinity.omos.utils.S3ImageFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WriteRecordViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val awsConnector: AWSConnector,
    private val musicRepository: MusicRepository,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private val _music = MutableStateFlow(
        MusicModel("", "", "", "", "", "")
    )
    val music = _music.asStateFlow()

    val category = MutableStateFlow(RecordCategory.A_LINE)
    val recordForm = MutableStateFlow(RecordForm.WRITE)

    val title = MutableStateFlow("")
    val imageUri = MutableStateFlow<Uri?>(null)
    val isPublic = MutableStateFlow(true)
    val lyrics = MutableStateFlow("")
    val contents = MutableStateFlow("")

    private val randomNumber = Random.nextInt(0, DEFAULT_IMAGE_COUNT) + 1
    val recordImageUrl = MutableStateFlow("${S3ImageFolder.RECORD.str}/${getDefaultRecordImageName(randomNumber)}.png")

    fun setCategory(newCategory: RecordCategory) {
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
        val imageUri = imageUri.value
        if (imageUri != null) {
            recordImageUrl.value = awsConnector.uploadFile(
                file = imageUri.toFile(),
                folder = S3ImageFolder.RECORD,
                userId = userId,
                bucketName = "omos-image"
            )
        }

        val record = SaveRecordRequest(
            category = category.value.name,
            isPublic = isPublic.value,
            musicId = music.value.musicId,
            recordContents = contents.value,
            recordImageUrl = recordImageUrl.value,
            recordTitle = title.value,
            userId = userId
        )
        viewModelScope.launch {
            recordRepository.saveRecord(record)
        }
    }

    companion object {
        private const val DEFAULT_IMAGE_COUNT = 3

        private fun getDefaultRecordImageName(num: Int) = "default_record_image_$num"
    }
}