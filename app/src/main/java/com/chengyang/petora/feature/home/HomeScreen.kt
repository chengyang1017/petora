package com.chengyang.petora.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onMapClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "🐶 Petora"
        )

        Button(
            onClick = onChatClick,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text("Chat with Petora")
        }

        Button(
            onClick = onMapClick,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text("Explore Map")
        }
    }
}