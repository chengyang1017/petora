package com.chengyang.petora.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                state.selectedLocation,
                12f,
            )
        }

    val markerState =
        rememberUpdatedMarkerState(
            position = state.selectedLocation,
        )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { location ->
                viewModel.selectLocation(
                    location = location,
                )
            },
        ) {
            Marker(
                state = markerState,
                title = "Selected location",
            )
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
        ) {
            Text(
                text = """
                    Latitude: ${state.selectedLocation.latitude}
                    Longitude: ${state.selectedLocation.longitude}
                """.trimIndent(),
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}