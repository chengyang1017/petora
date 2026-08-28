package com.chengyang.petora.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chengyang.petora.feature.map.data.PlaceRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MapUiState()
    )

    val uiState: StateFlow<MapUiState> = _uiState

    fun selectLocation(
        location: LatLng,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedLocation = location,
                selectedPlaceName = null,
                isLoadingPlace = true,
            )
        }

        viewModelScope.launch {
            try {
                val placeName = placeRepository.getPlaceName(
                    location = location,
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        selectedPlaceName = placeName,
                        isLoadingPlace = false,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedPlaceName = null,
                        isLoadingPlace = false,
                    )
                }
            }
        }
    }

    fun selectPlace(
        name: String,
        location: LatLng,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedLocation = location,
                selectedPlaceName = name,
                isLoadingPlace = false,
            )
        }
    }
}