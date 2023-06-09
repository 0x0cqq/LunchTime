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


    fun onClickLike(context: Context, postID: Int){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.likePost(
                    userData.data.first().userName,
                    postID)
                if (response.status){
                    // update likeCount and isLiked
                    val newPostDataList = uiState.value.postDataList.map{ postData ->
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
                        state.copy(postDataList = newPostDataList)
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
                    val newPostDataList = uiState.value.postDataList.map{ postData ->
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
                        state.copy(postDataList = newPostDataList)
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

    fun refresh(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isRefreshing = true)
            }
            val userData = context.userPreferencesStore
            try{

                val response = if (uiState.value.dropMenuIndex < 3) {
                    LunchTimeApi.retrofitService.getPostList(
                        name = userData.data.first().userName,
                        type = uiState.value.selectedIndex,
                        filter = uiState.value.dropMenuIndex
                    )
                } else {

                    LunchTimeApi.retrofitService.getPostListSearched(
                        name = userData.data.first().userName,
                        keyword = when(uiState.value.dropMenuIndex) {
                            4 -> "校园活动"
                            5 -> "失物招领"
                            6 -> "随便聊聊"
                            else -> ""
                        },
                        field = "tag",
                        type = uiState.value.selectedIndex,
                    )
                }

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
            } finally {
                _uiState.update {
                    it.copy(isRefreshing = false)
                }
            }
        }
    }
    fun onSwitchDropDownMenu(context: Context, index: Int){
        _uiState.update { state ->
            state.copy( dropMenuIndex = index)
        }
        this.refresh(context)
    }
    fun addRandomPost() {
        _uiState.update { state ->
            state.copy(postDataList = state.postDataList + PostData(title = "test", content = Random.nextInt().toString()))
        }
    }
}