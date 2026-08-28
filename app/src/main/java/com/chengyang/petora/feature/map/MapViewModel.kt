package com.chengyang.petora.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chengyang.petora.feature.map.data.PlaceRepository
import com.chengyang.petora.feature.map.data.PlaceSearchResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var searchJob: Job? = null

    private var searchRequestId = 0L

    fun onSearchQueryChanged(
        value: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = value,
                hasSearchCompleted = false,
            )
        }

        searchJob?.cancel()

        searchRequestId += 1

        val requestId = searchRequestId
        val query = value.trim()

        if (query.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    hasSearchCompleted = false,
                )
            }

            return
        }

        searchJob = viewModelScope.launch {
            delay(500)

            performSearch(
                query = query,
                requestId = requestId,
            )
        }
    }

    fun searchPlaces() {
        searchJob?.cancel()

        searchRequestId += 1

        val requestId = searchRequestId
        val query = _uiState.value.searchQuery.trim()

        if (query.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    hasSearchCompleted = false,
                )
            }

            return
        }

        searchJob = viewModelScope.launch {
            performSearch(
                query = query,
                requestId = requestId,
            )
        }
    }

    private suspend fun performSearch(
        query: String,
        requestId: Long,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isSearching = true,
                searchResults = emptyList(),
                hasSearchCompleted = false,
            )
        }

        try {
            val results = placeRepository.searchPlaces(
                query = query,
            )

            if (requestId != searchRequestId) {
                return
            }

            _uiState.update { currentState ->
                currentState.copy(
                    searchResults = results,
                    isSearching = false,
                    hasSearchCompleted = true,
                )
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            if (requestId != searchRequestId) {
                return
            }

            _uiState.update { currentState ->
                currentState.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    hasSearchCompleted = true,
                )
            }
        }
    }

    fun selectSearchResult(
        result: PlaceSearchResult,
    ) {
        searchJob?.cancel()

        searchRequestId += 1

        _uiState.update { currentState ->
            currentState.copy(
                selectedLocation = result.location,
                selectedPlaceName = result.name,
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false,
                hasSearchCompleted = false,
                isLoadingPlace = false,
            )
        }
    }

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