package com.thss.lunchtime.mainscreen.infopage

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyInfoPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyInfoPageState(InfoData(), listOf()))
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context) {
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            val userName = userData.data.first().userName
            try {
                val response = LunchTimeApi.retrofitService.getPostList(
                    name = userName,
                    type = 0,
                    targetName = userName
                )
                if (response.status) {
                    _uiState.update { state ->
                        state.copy(
                            infoData = state.infoData.copy(
                                ID = userName,
                            ),
                            postList = response.posts.map { it.toPostData() }
                        )
                    }
                } else {
                    Toast.makeText(context, "获取个人信息失败, ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }

        }
    }

}