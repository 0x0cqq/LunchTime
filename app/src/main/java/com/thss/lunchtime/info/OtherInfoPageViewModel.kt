package com.thss.lunchtime.info

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.mainscreen.infopage.MyInfoPageState
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toInfoData
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtherInfoPageViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(MyInfoPageState(InfoData(), listOf()))
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context, targetUserName: String){
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            try{
                val myUserName = userData.data.first().userName
                val response = LunchTimeApi.retrofitService.getUserInfo(
                    name = myUserName,
                    target_name = targetUserName
                )
                if (response.status) {
                    val info = response.userInfo.toInfoData()
                    _uiState.update { state ->
                        state.copy(
                            infoData = info.copy(ID = targetUserName)
                        )
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }

            try {
                val myUserName = userData.data.first().userName
                val response = LunchTimeApi.retrofitService.getPostList(
                    name = myUserName,
                    type = 0,
                    targetName = targetUserName
                )
                if (response.status) {
                    _uiState.update { state ->
                        state.copy(
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