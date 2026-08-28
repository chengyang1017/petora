package com.chengyang.petora.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chengyang.petora.core.places.PetoraPlaces
import com.chengyang.petora.feature.chat.PlaceContext
import com.chengyang.petora.feature.map.data.GooglePlaceRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch

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

    val coroutineScope = rememberCoroutineScope()

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

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Search places...",
                        )
                    },
                    singleLine = true,
                    enabled = !state.isSearching,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.searchPlaces()
                        },
                    ),
                )

                Button(
                    onClick = viewModel::searchPlaces,
                    enabled =
                        state.searchQuery.isNotBlank() &&
                                !state.isSearching,
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Text(
                        text = if (state.isSearching) {
                            "..."
                        } else {
                            "Search"
                        },
                    )
                }
            }

            if (state.isSearching) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Text(
                        text = "Searching places...",
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            if (
                state.searchResults.isNotEmpty() &&
                !state.isSearching
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                        ),
                    ) {
                        state.searchResults.forEach { result ->
                            TextButton(
                                onClick = {
                                    viewModel.selectSearchResult(
                                        result = result,
                                    )

                                    coroutineScope.launch {
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(
                                                result.location,
                                                16f,
                                            ),
                                            durationMs = 800,
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = result.name,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            }

            if (
                state.hasSearchCompleted &&
                state.searchResults.isEmpty() &&
                !state.isSearching
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Text(
                        text = "No places found",
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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
}