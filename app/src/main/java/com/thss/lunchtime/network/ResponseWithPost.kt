package com.thss.lunchtime.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWithPost(
    val status: Boolean,
    val message: String,
    @SerialName("post_id")
    val postId: Int? = null
)