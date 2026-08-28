package com.chengyang.petora.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chengyang.petora.core.network.PetoraNetwork
import com.chengyang.petora.feature.chat.data.ChatRepository
import com.chengyang.petora.feature.chat.data.RemoteChatRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository = RemoteChatRepository(
        chatApi = PetoraNetwork.chatApi,
    ),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState()
    )

    val uiState: StateFlow<ChatUiState> = _uiState

    private var previousResponseId: String? = null

    private var currentPlaceContext: PlaceContext? = null

    private var startedPlaceContext: PlaceContext? = null

    fun setPlaceContext(
        placeContext: PlaceContext?,
    ) {
        currentPlaceContext = placeContext
    }

    fun startPlaceExploration(
        placeContext: PlaceContext,
    ) {
        if (startedPlaceContext == placeContext) {
            return
        }

        startedPlaceContext = placeContext
        currentPlaceContext = placeContext

        previousResponseId = null

        _uiState.update { currentState ->
            currentState.copy(
                messages = emptyList(),
                input = "",
                errorMessage = null,
            )
        }

        requestReply(
            message = "Give me a short, friendly introduction to this place and tell me what makes it interesting.",
            showUserMessage = false,
        )
    }

    fun onInputChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                input = value,
            )
        }
    }

    fun sendMessage() {
        val message = _uiState.value.input.trim()

        if (message.isEmpty()) {
            return
        }

        requestReply(
            message = message,
            showUserMessage = true,
        )
    }

    private fun requestReply(
        message: String,
        showUserMessage: Boolean,
    ) {
        if (_uiState.value.isLoading) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                messages = if (showUserMessage) {
                    currentState.messages + ChatMessage(
                        text = message,
                        isFromUser = true,
                    )
                } else {
                    currentState.messages
                },
                input = if (showUserMessage) {
                    ""
                } else {
                    currentState.input
                },
                isLoading = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            try {
                val reply = chatRepository.getReply(
                    message = message,
                    previousResponseId = previousResponseId,
                    placeContext = currentPlaceContext,
                )

                previousResponseId = reply.responseId

                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + ChatMessage(
                            text = reply.text,
                            isFromUser = false,
                        ),
                        isLoading = false,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Something went wrong. Please try again.",
                    )
                }
            }
        }
    }
}