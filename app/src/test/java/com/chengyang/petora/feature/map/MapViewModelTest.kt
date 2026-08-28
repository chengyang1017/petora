package com.chengyang.petora.feature.map

import com.chengyang.petora.MainDispatcherRule
import com.chengyang.petora.feature.map.data.PlaceRepository
import com.chengyang.petora.feature.map.data.PlaceSearchResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
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

    @Test
    fun `search waits for debounce and only searches latest query`() = runTest {
        val repository = FakePlaceRepository(
            placeName = null,
        )

        val viewModel = MapViewModel(
            placeRepository = repository,
        )

        viewModel.onSearchQueryChanged(
            "Pet",
        )

        advanceTimeBy(300)

        viewModel.onSearchQueryChanged(
            "Petronas",
        )

        advanceTimeBy(499)
        runCurrent()

        assertEquals(
            emptyList<String>(),
            repository.searchQueries,
        )

        advanceTimeBy(1)
        advanceUntilIdle()

        assertEquals(
            listOf("Petronas"),
            repository.searchQueries,
        )
    }
}

private class FakePlaceRepository(
    private val placeName: String?,
) : PlaceRepository {

    var requestCount = 0
        private set

    val searchQueries =
        mutableListOf<String>()

    override suspend fun getPlaceName(
        location: LatLng,
    ): String? {
        requestCount += 1

        return placeName
    }

    override suspend fun searchPlaces(
        query: String,
    ): List<PlaceSearchResult> {
        searchQueries += query

        return emptyList()
    }
}