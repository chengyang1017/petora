package com.chengyang.petora.feature.map.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.kotlin.circularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitSearchByText
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

    override suspend fun searchPlaces(
        query: String,
    ): List<PlaceSearchResult> {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isEmpty()) {
            return emptyList()
        }

        val response = placesClient.awaitSearchByText(
            textQuery = trimmedQuery,
            placeFields = listOf(
                Place.Field.DISPLAY_NAME,
                Place.Field.LOCATION,
            ),
        ) {
            maxResultCount = 5
        }

        return response.places.mapNotNull { place ->
            val name = place.displayName
                ?: return@mapNotNull null

            val location = place.location
                ?: return@mapNotNull null

            PlaceSearchResult(
                name = name,
                location = location,
            )
        }
    }
}