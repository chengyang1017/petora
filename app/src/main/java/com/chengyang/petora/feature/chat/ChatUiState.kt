package com.chengyang.petora.feature.chat

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
)