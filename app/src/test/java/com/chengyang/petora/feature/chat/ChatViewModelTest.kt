package com.chengyang.petora.feature.chat

import com.chengyang.petora.MainDispatcherRule
import com.chengyang.petora.feature.chat.data.ChatReply
import com.chengyang.petora.feature.chat.data.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `sendMessage adds user message and pet reply`() = runTest {
        val repository = SuccessChatRepository()

        val viewModel = ChatViewModel(
            chatRepository = repository,
        )

        viewModel.onInputChanged("Hello")

        viewModel.sendMessage()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            2,
            state.messages.size,
        )

        assertEquals(
            "Hello",
            state.messages[0].text,
        )

        assertEquals(
            true,
            state.messages[0].isFromUser,
        )

        assertEquals(
            "Momo reply: Hello",
            state.messages[1].text,
        )

        assertEquals(
            false,
            state.messages[1].isFromUser,
        )

        assertEquals(
            "",
            state.input,
        )

        assertFalse(
            state.isLoading,
        )

        assertNull(
            state.errorMessage,
        )
    }

    @Test
    fun `sendMessage shows error when repository fails`() = runTest {
        val repository = ErrorChatRepository()

        val viewModel = ChatViewModel(
            chatRepository = repository,
        )

        viewModel.onInputChanged("Hello")

        viewModel.sendMessage()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            1,
            state.messages.size,
        )

        assertEquals(
            "Hello",
            state.messages[0].text,
        )

        assertFalse(
            state.isLoading,
        )

        assertEquals(
            "Something went wrong. Please try again.",
            state.errorMessage,
        )
    }

    @Test
    fun `second message uses previous response id`() = runTest {
        val repository = RecordingChatRepository()

        val viewModel = ChatViewModel(
            chatRepository = repository,
        )

        viewModel.onInputChanged(
            "My favorite animal is tiger"
        )

        viewModel.sendMessage()

        advanceUntilIdle()

        viewModel.onInputChanged(
            "What is my favorite animal?"
        )

        viewModel.sendMessage()

        advanceUntilIdle()

        assertEquals(
            2,
            repository.receivedPreviousResponseIds.size,
        )

        assertNull(
            repository.receivedPreviousResponseIds[0],
        )

        assertEquals(
            "response-1",
            repository.receivedPreviousResponseIds[1],
        )

        val state = viewModel.uiState.value

        assertEquals(
            4,
            state.messages.size,
        )
    }
}

private class SuccessChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
    ): ChatReply {
        return ChatReply(
            text = "Momo reply: $message",
            responseId = "response-test",
        )
    }
}

private class ErrorChatRepository : ChatRepository {

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
    ): ChatReply {
        throw IllegalStateException(
            "Fake repository failure"
        )
    }
}

private class RecordingChatRepository : ChatRepository {

    val receivedPreviousResponseIds =
        mutableListOf<String?>()

    private var responseCount = 0

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
    ): ChatReply {
        receivedPreviousResponseIds +=
            previousResponseId

        responseCount += 1

        return ChatReply(
            text = "Reply $responseCount",
            responseId = "response-$responseCount",
        )
    }
}