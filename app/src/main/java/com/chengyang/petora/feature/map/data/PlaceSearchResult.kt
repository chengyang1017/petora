package com.chengyang.petora.feature.map.data

import com.google.android.gms.maps.model.LatLng

data class PlaceSearchResult(
    val name: String,
    val location: LatLng,
)