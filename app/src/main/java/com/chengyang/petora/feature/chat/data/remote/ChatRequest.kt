package com.chengyang.petora.feature.chat.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val message: String,
    val petId: String,
    val previousResponseId: String? = null,
)