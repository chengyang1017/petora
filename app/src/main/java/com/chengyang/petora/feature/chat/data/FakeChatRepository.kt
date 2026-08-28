package com.chengyang.petora.feature.chat.data

import kotlinx.coroutines.delay

class FakeChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
    ): ChatReply {
        delay(1000)

        if (message == "/error") {
            throw IllegalStateException(
                "Fake AI request failed"
            )
        }

        return ChatReply(
            text = "Woof! You said: $message",
            responseId = null,
        )
    }
}