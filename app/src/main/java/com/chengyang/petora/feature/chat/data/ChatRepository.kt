package com.chengyang.petora.feature.chat.data

interface ChatRepository {

    suspend fun getReply(
        message: String,
    ): String
}