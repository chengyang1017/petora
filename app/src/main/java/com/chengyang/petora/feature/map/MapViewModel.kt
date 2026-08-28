package com.chengyang.petora.feature.map

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MapViewModel : ViewModel() {

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
            )
        }
    }
}