package com.thss.lunchtime.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.LunchTimeChatService
import com.thss.lunchtime.network.toChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChatPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ChatPageState())
    val uiState = _uiState.asStateFlow()


    private var chatService : LunchTimeChatService? = null

    fun updateInputValue(value: String) {
        _uiState.value = _uiState.value.copy(inputValue = value)
    }

    fun getOppositeUserInfo(context: Context, oppoSiteUserName: String) {
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            try {
                val response = LunchTimeApi.retrofitService.getUserInfo(userData.data.first().userName, oppoSiteUserName)
                if (response.status) {
                    _uiState.value = _uiState.value.copy(
                        oppositeUserName = response.userInfo.userName,
                    )
                } else {
                    Log.d("LunchTime Chat", "Get opposite user info failed")
                    Toast.makeText(context, "无法获取用户信息", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun connect(senderName: String, receiverName: String) {
        // close previous connection
        viewModelScope.launch {
            if (chatService != null) {
                chatService!!.close()
            }
            chatService = LunchTimeChatService(senderName, receiverName)
            // open a new one
            chatService!!.connect { chatResponse ->
                when(chatResponse.type) {
                    "message" -> {
                        _uiState.value = _uiState.value.copy(
                            messageList = listOf(chatResponse.message!!.toChatData()) + uiState.value.messageList
                        )
                    }
                    "history" -> {
                        _uiState.value = _uiState.value.copy(
                            messageList = chatResponse.messageList!!.map { it.toChatData() },
                        )
                    }
                    else -> {
                        Log.d("LunchTime Chat", "Unknown type ${chatResponse.type}")
                    }
                }
            }
        }
    }
    fun send(value: String) {
        viewModelScope.launch {
            Log.d("LunchTime Chat", "Emit $value")
            chatService!!.send({
                _uiState.value = _uiState.value.copy(
                    messageList = (uiState.value.messageList + it.toChatData())
                )
            },value)
            _uiState.value = _uiState.value.copy(inputValue = "")
        }
    }

}