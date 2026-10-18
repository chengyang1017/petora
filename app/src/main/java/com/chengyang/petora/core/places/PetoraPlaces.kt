package com.chengyang.petora.core.places

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PetoraPlaces {

    fun createClient(
        context: Context,
    ): PlacesClient {
        return Places.createClient(
            context.applicationContext,
        )
    }
}