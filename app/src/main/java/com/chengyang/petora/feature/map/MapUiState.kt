package com.chengyang.petora.feature.map

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val selectedLocation: LatLng = LatLng(
        3.1390,
        101.6869,
    ),
    val selectedPlaceName: String? = null,
    val isLoadingPlace: Boolean = false,

    val searchQuery: String = "",
)