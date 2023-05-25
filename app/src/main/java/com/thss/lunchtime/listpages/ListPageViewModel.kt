package com.thss.lunchtime.listpages

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toInfoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ListPageViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ListPageState())
    val uiState = _uiState.asStateFlow()

    /** type: 0->followingList 1->fansList 2->BlockList **/
    fun refresh(context: Context, userName: String, type: Int){
        viewModelScope.launch {
            try{
                val response = LunchTimeApi.retrofitService.getAttentionList(userName, type)
                if (response.status){
                    _uiState.update { state ->
                        state.copy(
                            type = type,
                            userList = response.userList.map{ userInfo ->
                                userInfo.toInfoData()
                            }
                        )
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