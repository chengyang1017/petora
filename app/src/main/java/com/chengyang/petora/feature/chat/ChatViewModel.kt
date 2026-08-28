package com.chengyang.petora.feature.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState()
    )

    val uiState: StateFlow<ChatUiState> = _uiState

    fun onInputChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                input = value
            )
        }
    }

    fun sendMessage() {
        val message = _uiState.value.input.trim()

        if (message.isEmpty()) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + ChatMessage(
                    text = message,
                    isFromUser = true,
                ),
                input = "",
            )
        }
    }
}