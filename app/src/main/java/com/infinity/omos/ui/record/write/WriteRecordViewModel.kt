package com.infinity.omos.ui.record.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.repository.music.MusicRepository
import com.infinity.omos.ui.record.Category
import com.infinity.omos.ui.record.RecordForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WriteRecordViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _music = MutableStateFlow(
        MusicModel("", "", "", "", "", "")
    )
    val music = _music.asStateFlow()

    val category = MutableStateFlow(Category.A_LINE)
    val recordForm = MutableStateFlow(RecordForm.WRITE)

    val title = MutableStateFlow("")
    val contents = MutableStateFlow("")
    val isPublic = MutableStateFlow(true)

    fun setCategory(newCategory: Category) {
        category.value = newCategory
    }

    fun setRecordForm(newRecordForm: RecordForm) {
        recordForm.value = newRecordForm
    }

    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    fun setContents(newContent: String) {
        contents.value = newContent
    }

    fun changeLockState() {
        isPublic.value = !isPublic.value
    }

    fun fetchMusic(musicId: String) {
        viewModelScope.launch {
            musicRepository.getMusic(musicId).mapCatching { it.toPresentation() }
                .onSuccess { _music.value = it }
                .onFailure { Timber.d("Error") }
        }
    }
}