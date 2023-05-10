package com.thss.lunchtime.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val status: Boolean,
    val message: String
)
