package com.thss.lunchtime.search

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SearchPageState())
    val uiState = _uiState.asStateFlow()

    fun onClickSearch(context: Context, field: Int, keyword: String){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            val fieldText = when(field){
                0 -> "all"
                1 -> "username"
                2 -> "content"
                3 -> "title"
                4 -> "tag"
                else -> "all"
            }
            try{
                val response = LunchTimeApi.retrofitService.getPostListSearched(
                    name = userData.data.first().userName,
                    field = fieldText,
                    keyword = keyword
                )
                if (response.status){
                    _uiState.update { state ->
                        state.copy(postDataList = response.posts.map { post -> post.toPostData() })
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
}