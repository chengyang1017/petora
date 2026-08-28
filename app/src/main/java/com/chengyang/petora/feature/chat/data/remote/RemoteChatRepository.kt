package com.chengyang.petora.feature.chat.data

import com.chengyang.petora.feature.chat.data.remote.ChatApi
import com.chengyang.petora.feature.chat.data.remote.ChatRequest

class RemoteChatRepository(
    private val chatApi: ChatApi,
) : ChatRepository {

    override suspend fun getReply(
        message: String,
    ): String {
        val response = chatApi.sendMessage(
            request = ChatRequest(
                message = message,
                petId = "momo",
            ),
        )

        return response.reply
    }
}