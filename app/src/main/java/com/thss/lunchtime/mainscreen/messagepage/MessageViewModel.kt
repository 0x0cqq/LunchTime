package com.thss.lunchtime.mainscreen.messagepage

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toNoticeData
import com.thss.lunchtime.NoticeData
import com.thss.lunchtime.network.toChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MessageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MessagepageState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun refresh(context: Context) {
        viewModelScope.launch{
            _uiState.update {
                it.copy(isRefreshing = true)
            }
            val userData = context.userPreferencesStore
            try{
                if(uiState.value.selectedIndex < 2) {
                    val response = LunchTimeApi.retrofitService.getNotice(
                        userData.data.first().userName,
                        uiState.value.selectedIndex + 1
                    )
                    if (response.status) {
                        _uiState.update { state ->
                            val updateNoticeList =
                                response.noticeList.map { notice -> notice.toNoticeData(uiState.value.selectedIndex) }
                            val newNoticeDataLists = mutableListOf<List<NoticeData>>()
                            for (i in uiState.value.noticeDataLists.indices) {
                                if (i == uiState.value.selectedIndex) {
                                    newNoticeDataLists.add(updateNoticeList)
                                } else {
                                    newNoticeDataLists.add(uiState.value.noticeDataLists[i])
                                }
                            }
                            state.copy(noticeDataLists = newNoticeDataLists)
                        }
                    } else {
                        Toast.makeText(context, "获取通知失败", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // 聊天
                    val response = LunchTimeApi.retrofitService.getChatList(userData.data.first().userName)
                    if (response.status) {
                        _uiState.update { state ->
                            val updateChatList = response.chatList.map { chat -> chat.toChatData() }.map {
                                NoticeData(
                                    it.userAvatar,
                                    it.userName,
                                    it.time,
                                    3,
                                    it.message,
                                )
                            }
                            val newNoticeDataLists = mutableListOf<List<NoticeData>>()
                            for (i in uiState.value.noticeDataLists.indices) {
                                if (i == uiState.value.selectedIndex) {
                                    newNoticeDataLists.add(updateChatList)
                                } else {
                                    newNoticeDataLists.add(uiState.value.noticeDataLists[i])
                                }
                            }
                            state.copy(noticeDataLists = newNoticeDataLists)
                        }
                    } else {
                        Toast.makeText(context, "获取聊天失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e : IOException){
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
            _uiState.update {
                it.copy(isRefreshing = false)
            }
        }
    }
}