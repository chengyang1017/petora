package com.chengyang.petora.feature.map

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val selectedLocation: LatLng = LatLng(
        3.1390,
        101.6869,
    ),
)