package com.chengyang.petora.feature.map

import com.chengyang.petora.MainDispatcherRule
import com.chengyang.petora.feature.map.data.PlaceRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `selectLocation updates selected location and place name`() = runTest {
        val repository = FakePlaceRepository()

        val viewModel = MapViewModel(
            placeRepository = repository,
        )

        val location = LatLng(
            3.1579,
            101.7123,
        )

        viewModel.selectLocation(
            location = location,
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            location,
            state.selectedLocation,
        )

        assertEquals(
            "Petronas Twin Towers",
            state.selectedPlaceName,
        )

        assertFalse(
            state.isLoadingPlace,
        )
    }
}

private class FakePlaceRepository : PlaceRepository {

    override suspend fun getPlaceName(
        location: LatLng,
    ): String {
        return "Petronas Twin Towers"
    }
}