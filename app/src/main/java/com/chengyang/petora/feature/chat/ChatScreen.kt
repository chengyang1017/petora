package com.chengyang.petora.feature.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(
    placeContext: PlaceContext? = null,
    viewModel: ChatViewModel = viewModel(),
) {
    LaunchedEffect(placeContext) {
        viewModel.setPlaceContext(
            placeContext = placeContext,
        )
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreenContent(
        state = state,
        placeContext = placeContext,
        onInputChanged = viewModel::onInputChanged,
        onSendClick = viewModel::sendMessage,
    )
}

@Composable
private fun ChatScreenContent(
    state: ChatUiState,
    placeContext: PlaceContext?,
    onInputChanged: (String) -> Unit,
    onSendClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
    ) {
        Text(
            text = "Chat with Momo",
            style = MaterialTheme.typography.headlineSmall,
        )

        if (placeContext != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                ) {
                    Text(
                        text = "Exploring",
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Text(
                        text = placeContext.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.messages) { message ->
                ChatMessageItem(
                    message = message,
                )
            }

            if (state.isLoading) {
                item {
                    Text(
                        text = "Momo is thinking...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        state.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.input,
                onValueChange = onInputChanged,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Say something...")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onSendClick()
                    },
                ),
            )

            Button(
                onClick = onSendClick,
                enabled = !state.isLoading,
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isFromUser) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        },
    ) {
        Card {
            Text(
                text = if (message.isFromUser) {
                    AnnotatedString(message.text)
                } else {
                    parseBoldMarkdown(
                        text = message.text,
                    )
                },
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp,
                ),
            )
        }
    }
}

private fun parseBoldMarkdown(
    text: String,
): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        while (currentIndex < text.length) {
            val boldStart = text.indexOf(
                "**",
                startIndex = currentIndex,
            )

            if (boldStart == -1) {
                append(
                    text.substring(currentIndex)
                )
                break
            }

            append(
                text.substring(
                    currentIndex,
                    boldStart,
                )
            )

            val boldEnd = text.indexOf(
                "**",
                startIndex = boldStart + 2,
            )

            if (boldEnd == -1) {
                append(
                    text.substring(boldStart)
                )
                break
            }

            pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                )
            )

            append(
                text.substring(
                    boldStart + 2,
                    boldEnd,
                )
            )

            pop()

            currentIndex = boldEnd + 2
        }
    }
}