package com.chengyang.petora.feature.map.data

import com.google.android.gms.maps.model.LatLng

interface PlaceRepository {

    suspend fun getPlaceName(
        location: LatLng,
    ): String?
}