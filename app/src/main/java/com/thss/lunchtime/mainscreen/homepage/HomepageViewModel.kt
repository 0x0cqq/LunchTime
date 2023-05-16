package com.thss.lunchtime.mainscreen.homepage

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

class HomepageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomepageState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun refresh(context: Context) {
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.getPostList(
                    userData.data.first().userName,
                    uiState.value.selectedIndex
                )
                if (response.status) { // valid response
                    _uiState.update { state ->
                        state.copy(postDataList =
                            response.posts.map { post ->
                                post.toPostData()
                            }
                        )
                    }
                } else { // invalid response
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }

            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addRandomPost() {
        _uiState.update { state ->
            state.copy(postDataList = state.postDataList + PostData(title = "test", content = Random.nextInt().toString()))
        }
    }
}