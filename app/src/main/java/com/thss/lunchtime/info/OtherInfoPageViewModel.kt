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
import java.io.IOException

class OtherInfoPageViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(MyInfoPageState(InfoData(), listOf()))
    val uiState = _uiState.asStateFlow()

    fun onClickRelation(context: Context){
        if (uiState.value.infoData.relation == 3){
            onClickBlock(context)
        }
        else{
            viewModelScope.launch{
                val userData = context.userPreferencesStore
                try{
                    val response = LunchTimeApi.retrofitService.followUser(
                        name = userData.data.first().userName,
                        targetName = uiState.value.infoData.ID
                    )
                    if (response.status){
                        if (response.result == 1){
                            _uiState.update{state ->
                                state.copy(
                                    infoData = uiState.value.infoData.copy(
                                        relation = 2,
                                        fansCnt = uiState.value.infoData.fansCnt + 1
                                    )
                                )
                            }
                        } else{
                            _uiState.update{state ->
                                state.copy(
                                    infoData = uiState.value.infoData.copy(
                                        relation = 1,
                                        fansCnt = uiState.value.infoData.fansCnt - 1
                                    )
                                )
                            }
                        }
                    }
                } catch (e : IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun onClickBlock(context: Context){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.blockUser(
                    name = userData.data.first().userName,
                    targetName = uiState.value.infoData.ID
                )
                if (response.status){
                    if(response.result == 1){
                        _uiState.update{state ->
                            state.copy(
                                infoData = uiState.value.infoData.copy(relation = 3)
                            )
                        }
                    } else {
                        _uiState.update{state ->
                            state.copy(
                                infoData = uiState.value.infoData.copy(relation = 1)
                            )
                        }
                    }
                }
            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
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
            } catch (e : IOException) {
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

    fun refresh(context: Context, targetUserName: String){
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoaded = false)
            }
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
                    targetName = targetUserName,
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
                    state.copy(isLoaded = true)
                }
            }
        }
    }
}