package com.chengyang.petora.feature.chat

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
)