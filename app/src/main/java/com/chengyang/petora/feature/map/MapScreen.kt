package com.chengyang.petora.feature.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
}