package com.thss.lunchtime.mainscreen.messagepage

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toNoticeData
import com.thss.lunchtime.noticeData
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
                val response = LunchTimeApi.retrofitService.getNotice(userData.data.first().userName, uiState.value.selectedIndex + 1)
                if (response.status) {
                    _uiState.update { state ->
                        val updateNoticeList = response.noticeList.map{ notice -> notice.toNoticeData(uiState.value.selectedIndex)}
                        val newNoticeDataLists = mutableListOf<List<noticeData>>()
                        for (i in uiState.value.NoticeDataLists.indices){
                            if (i == uiState.value.selectedIndex){
                                newNoticeDataLists.add(updateNoticeList)
                            }
                            else{
                                newNoticeDataLists.add(uiState.value.NoticeDataLists[i])
                            }
                        }
                        state.copy( NoticeDataLists = newNoticeDataLists)
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