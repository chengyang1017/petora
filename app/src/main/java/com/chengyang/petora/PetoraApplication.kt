package com.chengyang.petora

import android.app.Application
import com.google.android.libraries.places.api.Places

class PetoraApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.MAPS_API_KEY

        if (
            apiKey.isNotBlank() &&
            apiKey != "DEFAULT_API_KEY"
        ) {
            Places.initializeWithNewPlacesApiEnabled(
                applicationContext,
                apiKey,
            )
        }
    }
}