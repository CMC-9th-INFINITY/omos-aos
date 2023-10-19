package com.infinity.omos.ui.record.write

import androidx.lifecycle.ViewModel
import com.infinity.omos.ui.record.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WriteRecordViewModel @Inject constructor(

) : ViewModel() {

    val category = MutableStateFlow(Category.A_LINE)

    fun setCategory(newCategory: Category) {
        category.value = newCategory
    }
}