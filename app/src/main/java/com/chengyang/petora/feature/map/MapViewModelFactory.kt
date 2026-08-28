package com.chengyang.petora.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chengyang.petora.feature.map.data.PlaceRepository

class MapViewModelFactory(
    private val placeRepository: PlaceRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        if (
            modelClass.isAssignableFrom(
                MapViewModel::class.java
            )
        ) {
            return MapViewModel(
                placeRepository = placeRepository,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}