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
        val repository = FakePlaceRepository(
            placeName = "Petronas Twin Towers",
        )

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

        assertEquals(
            1,
            repository.requestCount,
        )
    }

    @Test
    fun `selectPlace uses POI directly without place lookup`() {
        val repository = FakePlaceRepository(
            placeName = "Should not be used",
        )

        val viewModel = MapViewModel(
            placeRepository = repository,
        )

        val location = LatLng(
            3.1478,
            101.6953,
        )

        viewModel.selectPlace(
            name = "Central Market",
            location = location,
        )

        val state = viewModel.uiState.value

        assertEquals(
            location,
            state.selectedLocation,
        )

        assertEquals(
            "Central Market",
            state.selectedPlaceName,
        )

        assertFalse(
            state.isLoadingPlace,
        )

        assertEquals(
            0,
            repository.requestCount,
        )
    }
}

private class FakePlaceRepository(
    private val placeName: String?,
) : PlaceRepository {

    var requestCount = 0
        private set

    override suspend fun getPlaceName(
        location: LatLng,
    ): String? {
        requestCount += 1

        return placeName
    }
}