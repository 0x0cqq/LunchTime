package com.thss.lunchtime.mainscreen.infopage

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toInfoData
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MyInfoPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyInfoPageState(InfoData(), listOf()))
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context) {
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            val userName = userData.data.first().userName
            try{
                val response = LunchTimeApi.retrofitService.getUserInfo(
                    name = userName,
                    target_name = userName
                )
                if (response.status) {
                    val info = response.userInfo.toInfoData()
                    _uiState.update { state ->
                        state.copy(
                            infoData = info.copy(ID = userName)
                        )
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }

            try {
                val response = LunchTimeApi.retrofitService.getPostList(
                    name = userName,
                    type = 0,
                    targetName = userName,
                    filter = 0
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
            } finally {
                _uiState.update { state ->
                    state.copy(
                        isLoaded = true
                    )
                }
            }
        }
    }

    fun onClickLike(context: Context, postID: Int){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.likePost(
                    userData.data.first().userName,
                    postID)
                if (response.status){
                    // update likeCount and isLiked
                    val newPostDataList = uiState.value.postList.map{ postData ->
                        if(postData.postID == postID){
                            if(response.result == 1){
                                postData.copy(likeCount = postData.likeCount + 1, isLiked = true)
                            }
                            else{
                                postData.copy(likeCount = postData.likeCount - 1, isLiked = false)
                            }
                        }
                        else{
                            postData
                        }
                    }
                    _uiState.update{state ->
                        state.copy(postList = newPostDataList)
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            }catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickStar(context: Context, postID: Int){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.starPost(
                    userData.data.first().userName,
                    postID)
                if (response.status){
                    // update starCount and isStared
                    val newPostDataList = uiState.value.postList.map{ postData ->
                        if(postData.postID == postID){
                            if(response.result == 1){
                                postData.copy(starCount = postData.starCount + 1, isStared = true)
                            }
                            else{
                                postData.copy(starCount = postData.starCount - 1, isStared = false)
                            }
                        }
                        else{
                            postData
                        }
                    }
                    _uiState.update{state ->
                        state.copy(postList = newPostDataList)
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }
}