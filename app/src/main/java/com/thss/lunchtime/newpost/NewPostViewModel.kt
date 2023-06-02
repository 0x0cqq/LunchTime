package com.thss.lunchtime.newpost

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.onebone.parvenu.ParvenuEditorValue
import java.io.File

class NewPostViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewPostData())
    val uiState = _uiState.asStateFlow()

    fun setTitle(value: String) {
        _uiState.update { state ->
            state.copy(title = value)
        }
    }
    fun setContent(value: String) {
        _uiState.update { state ->
            state.copy(content = value)
        }
    }
    fun setRichContent(value: ParvenuEditorValue) {
        _uiState.update { state ->
            state.copy(richContent = value)
        }
    }

    fun removeImage(index: Int) {
        _uiState.update { state ->
            state.copy(selectedImgUris = state.selectedImgUris.filterIndexed { i, _ -> i != index })
        }
    }
    fun appendImages(value: List<ImageBitmap>) {
        _uiState.update { state ->
            state.copy(selectedImgUris = state.selectedImgUris + value)
        }
    }

    fun appendVideos(value: List<File>) {
        _uiState.update { state ->
            state.copy(selectedVideoUris = state.selectedVideoUris + value)
        }
    }
    fun setLocation(value: String) {
        _uiState.update { state ->
            state.copy(location = value, isLocationUsed = value.isNotEmpty())
        }
    }

    fun setTag(value: String) {
        _uiState.update { state ->
            state.copy(tag = value, isTagUsed = value.isNotEmpty())
        }
    }

    fun setVideoFlag (flag: Boolean){
        _uiState.update { state ->
            state.copy(isVideo = flag)
        }
    }


}