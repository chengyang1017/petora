package com.chengyang.petora.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chengyang.petora.feature.chat.data.ChatRepository
import com.chengyang.petora.feature.chat.data.FakeChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository = FakeChatRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState()
    )

    val uiState: StateFlow<ChatUiState> = _uiState

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

        if (_uiState.value.isLoading) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + ChatMessage(
                    text = message,
                    isFromUser = true,
                ),
                input = "",
                isLoading = true,
            )
        }

        viewModelScope.launch {
            val reply = chatRepository.getReply(
                message = message,
            )

            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + ChatMessage(
                        text = reply,
                        isFromUser = false,
                    ),
                    isLoading = false,
                )
            }
        }
    }
}