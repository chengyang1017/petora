package com.chengyang.petora.feature.chat.data

import com.chengyang.petora.feature.chat.PlaceContext
import com.chengyang.petora.feature.chat.data.remote.ChatApi
import com.chengyang.petora.feature.chat.data.remote.ChatRequest
import com.chengyang.petora.feature.chat.data.remote.PlaceRequest

class RemoteChatRepository(
    private val chatApi: ChatApi,
) : ChatRepository {

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
        placeContext: PlaceContext?,
    ): ChatReply {
        val response = chatApi.sendMessage(
            request = ChatRequest(
                message = message,
                petId = "momo",
                previousResponseId = previousResponseId,
                place = placeContext?.let { place ->
                    PlaceRequest(
                        name = place.name,
                        latitude = place.latitude,
                        longitude = place.longitude,
                    )
                },
            ),
        )

        return ChatReply(
            text = response.reply,
            responseId = response.responseId,
        )
    }
}