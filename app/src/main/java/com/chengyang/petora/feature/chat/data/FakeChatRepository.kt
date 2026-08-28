package com.chengyang.petora.feature.chat.data

import kotlinx.coroutines.delay

class FakeChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
    ): String {
        delay(1000)

        return "Woof! You said: $message"
    }
}