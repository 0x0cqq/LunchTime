package com.thss.lunchtime.network
import androidx.core.net.toUri
import com.thss.lunchtime.component.InfoData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_image")
    val userImage: String,
    @SerialName("user_description")
    val userDescription: String,
    @SerialName("follow_count")
    val followCount: Int,
    @SerialName("fans_count")
    val fansCount: Int,
    @SerialName("is_following")
    val isFollowing: Boolean,
    @SerialName("is_hating")
    val isHating: Boolean
)

fun UserInfo.toInfoData(): InfoData {
    var relation = 1
    if (isFollowing){
        relation = 2
    }
    if(isHating){
        relation = 3
    }
    return InfoData(
        Avatar = userImage.toUri(),
        ID = userName,
        SelfIntro = userDescription,
        followCnt = followCount,
        fansCnt = fansCount,
        relation = relation
    )
}