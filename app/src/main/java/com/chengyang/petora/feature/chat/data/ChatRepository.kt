package com.chengyang.petora.feature.chat.data

import com.chengyang.petora.feature.chat.PlaceContext

interface ChatRepository {

    suspend fun getReply(
        message: String,
        previousResponseId: String?,
        placeContext: PlaceContext?,
    ): ChatReply
}