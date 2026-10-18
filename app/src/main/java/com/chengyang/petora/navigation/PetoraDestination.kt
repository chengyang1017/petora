package com.chengyang.petora.navigation

import android.net.Uri

object PetoraDestination {

    const val HOME = "home"
    const val CHAT = "chat"
    const val MAP = "map"

    const val PLACE_NAME = "placeName"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"

    const val CHAT_WITH_PLACE =
        "$CHAT?$PLACE_NAME={$PLACE_NAME}" +
                "&$LATITUDE={$LATITUDE}" +
                "&$LONGITUDE={$LONGITUDE}"

    fun chatWithPlace(
        placeName: String,
        latitude: Double,
        longitude: Double,
    ): String {
        return "$CHAT" +
                "?$PLACE_NAME=${Uri.encode(placeName)}" +
                "&$LATITUDE=$latitude" +
                "&$LONGITUDE=$longitude"
    }
}