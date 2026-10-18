package com.chengyang.petora.feature.chat

import com.chengyang.petora.MainDispatcherRule
import com.chengyang.petora.feature.chat.data.ChatReply
import com.chengyang.petora.feature.chat.data.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatPlaceExplorationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `switching place starts a new conversation`() = runTest {
        val repository = RecordingPlaceChatRepository()

        val viewModel = ChatViewModel(
            chatRepository = repository,
        )

        val firstPlace = PlaceContext(
            name = "Petronas Twin Towers",
            latitude = 3.1579,
            longitude = 101.7123,
        )

        val secondPlace = PlaceContext(
            name = "Central Market",
            latitude = 3.1457,
            longitude = 101.6953,
        )

        viewModel.startPlaceExploration(
            placeContext = firstPlace,
        )

        advanceUntilIdle()

        viewModel.onInputChanged(
            "What is nearby?"
        )

        viewModel.sendMessage()

        advanceUntilIdle()

        viewModel.startPlaceExploration(
            placeContext = secondPlace,
        )

        advanceUntilIdle()

        assertEquals(
            listOf(
                null,
                "response-1",
                null,
            ),
            repository.receivedPreviousResponseIds,
        )

        assertEquals(
            listOf(
                "Petronas Twin Towers",
                "Petronas Twin Towers",
                "Central Market",
            ),
            repository.receivedPlaceNames,
        )

        val state = viewModel.uiState.value

        assertEquals(
            1,
            state.messages.size,
        )

        assertEquals(
            "Reply 3",
            state.messages.first().text,
        )
    }
}

private class RecordingPlaceChatRepository :
    ChatRepository {

    val receivedPreviousResponseIds =
        mutableListOf<String?>()

    val receivedPlaceNames =
        mutableListOf<String?>()

    private var responseCount = 0

    override suspend fun getReply(
        message: String,
        previousResponseId: String?,
        placeContext: PlaceContext?,
    ): ChatReply {
        receivedPreviousResponseIds +=
            previousResponseId

        receivedPlaceNames +=
            placeContext?.name

        responseCount += 1

        return ChatReply(
            text = "Reply $responseCount",
            responseId = "response-$responseCount",
        )
    }
}