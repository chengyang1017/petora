package com.chengyang.petora.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onMapClick: () -> Unit,
    state: HomeUiState = HomeUiState(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "🐶",
            style = MaterialTheme.typography.displayLarge,
        )

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        Text(
            text = state.petName,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(
            modifier = Modifier.height(8.dp),
        )

        Text(
            text = state.greeting,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(32.dp),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = "Talk to ${state.petName}",
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "Chat about anything, anytime.",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Button(
                    onClick = onChatClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Text("Start chatting")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        OutlinedButton(
            onClick = onMapClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("🗺 Explore the world")
        }
    }
}