package com.chengyang.petora.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chengyang.petora.core.places.PetoraPlaces
import com.chengyang.petora.feature.chat.PlaceContext
import com.chengyang.petora.feature.map.data.GooglePlaceRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapScreen(
    onAskMomoClick: (PlaceContext) -> Unit,
) {
    val context = LocalContext.current

    val factory = remember {
        MapViewModelFactory(
            placeRepository = GooglePlaceRepository(
                placesClient = PetoraPlaces.createClient(
                    context = context,
                ),
            ),
        )
    }

    val viewModel: MapViewModel = viewModel(
        factory = factory,
    )

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
            onPOIClick = { poi ->
                viewModel.selectPlace(
                    name = poi.name,
                    location = poi.latLng,
                )
            },
        ) {
            Marker(
                state = markerState,
                title = state.selectedPlaceName
                    ?: "Selected location",
            )
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                when {
                    state.isLoadingPlace -> {
                        Text(
                            text = "Finding this place...",
                        )
                    }

                    state.selectedPlaceName != null -> {
                        Text(
                            text = state.selectedPlaceName!!,
                        )
                    }

                    else -> {
                        Text(
                            text = "No place found",
                        )
                    }
                }

                Text(
                    text = "Latitude: ${state.selectedLocation.latitude}",
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    text = "Longitude: ${state.selectedLocation.longitude}",
                )

                Button(
                    onClick = {
                        val placeName =
                            state.selectedPlaceName
                                ?: return@Button

                        onAskMomoClick(
                            PlaceContext(
                                name = placeName,
                                latitude = state.selectedLocation.latitude,
                                longitude = state.selectedLocation.longitude,
                            ),
                        )
                    },
                    enabled =
                        state.selectedPlaceName != null &&
                                !state.isLoadingPlace,
                    modifier = Modifier.padding(top = 12.dp),
                ) {
                    Text(
                        text = "Ask Momo",
                    )
                }
            }
        }
    }
}