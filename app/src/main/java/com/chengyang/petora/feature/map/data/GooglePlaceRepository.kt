package com.chengyang.petora.feature.map.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.kotlin.circularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitSearchNearby

class GooglePlaceRepository(
    private val placesClient: PlacesClient,
) : PlaceRepository {

    override suspend fun getPlaceName(
        location: LatLng,
    ): String? {
        val response = placesClient.awaitSearchNearby(
            locationRestriction = circularBounds(
                center = location,
                radius = 100.0,
            ),
            placeFields = listOf(
                Place.Field.DISPLAY_NAME,
            ),
        ) {
            maxResultCount = 1
        }

        return response.places
            .firstOrNull()
            ?.displayName
    }
}