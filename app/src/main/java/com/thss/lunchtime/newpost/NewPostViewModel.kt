package com.thss.lunchtime.newpost

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.network.LunchTimeApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.onebone.parvenu.ParvenuEditorValue
import java.io.File

class NewPostViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewPostData())
    val uiState = _uiState.asStateFlow()
    fun initData(context: Context, userName: String) {
        viewModelScope.launch {
            try {
                val response = LunchTimeApi.retrofitService.getUserInfo(userName, userName)
                if (response.status) {
                    _uiState.update { state ->
                        state.copy(avatarUri = response.userInfo.userImage.toUri())
                    }
                } else {
                    Toast.makeText(context, "错误， ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clear() {
        _uiState.update { _ ->
            NewPostData()
        }
    }

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
            state.copy(
                selectedImgUris = state.selectedImgUris.filterIndexed { i, _ -> i != index },
                isVideo = false,
                selectedVideoUris = listOf()
            )
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