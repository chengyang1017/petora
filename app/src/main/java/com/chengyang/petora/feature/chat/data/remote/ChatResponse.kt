package com.chengyang.petora.feature.chat.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val reply: String,
)