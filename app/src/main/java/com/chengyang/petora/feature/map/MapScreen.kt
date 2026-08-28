package com.chengyang.petora.feature.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapScreen() {
    val kualaLumpur = LatLng(
        3.1390,
        101.6869,
    )

    var selectedLocation by remember {
        mutableStateOf(kualaLumpur)
    }

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                kualaLumpur,
                12f,
            )
        }

    val markerState = rememberUpdatedMarkerState(
        position = selectedLocation,
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { location ->
            selectedLocation = location
        },
    ) {
        Marker(
            state = markerState,
            title = "Selected location",
        )
    }
}