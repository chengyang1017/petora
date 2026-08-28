package com.chengyang.petora.feature.map

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewModelTest {

    @Test
    fun `selectLocation updates selected location`() {
        val viewModel = MapViewModel()

        val location = LatLng(
            3.1579,
            101.7123,
        )

        viewModel.selectLocation(
            location = location,
        )

        val state = viewModel.uiState.value

        assertEquals(
            location,
            state.selectedLocation,
        )
    }
}