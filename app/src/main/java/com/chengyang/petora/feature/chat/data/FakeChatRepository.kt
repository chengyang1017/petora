package com.chengyang.petora.feature.chat.data

import kotlinx.coroutines.delay

class FakeChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
    ): String {
        delay(1000)

        if (message == "/error") {
            throw IllegalStateException("Fake AI request failed")
        }

        return "Woof! You said: $message"
    }
}