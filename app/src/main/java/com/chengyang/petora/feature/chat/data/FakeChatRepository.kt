package com.chengyang.petora.feature.chat.data

import com.chengyang.petora.feature.chat.PlaceContext
import kotlinx.coroutines.delay

class FakeChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
        placeContext: PlaceContext?,
    ): ChatReply {
        delay(1000)

        if (message == "/error") {
            throw IllegalStateException(
                "Fake AI request failed"
            )
        }

        val placeText = if (placeContext != null) {
            " You are looking at ${placeContext.name}."
        } else {
            ""
        }

        return ChatReply(
            text = "Woof! You said: $message$placeText",
            responseId = null,
        )
    }
}