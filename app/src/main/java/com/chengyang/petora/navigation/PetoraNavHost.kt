package com.chengyang.petora.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chengyang.petora.feature.chat.ChatScreen
import com.chengyang.petora.feature.chat.PlaceContext
import com.chengyang.petora.feature.home.HomeScreen
import com.chengyang.petora.feature.map.MapScreen

@Composable
fun PetoraNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = PetoraDestination.HOME,
    ) {
        composable(
            route = PetoraDestination.HOME,
        ) {
            HomeScreen(
                onChatClick = {
                    navController.navigate(
                        PetoraDestination.CHAT
                    )
                },
                onMapClick = {
                    navController.navigate(
                        PetoraDestination.MAP
                    )
                },
            )
        }

        composable(
            route = PetoraDestination.CHAT_WITH_PLACE,
            arguments = listOf(
                navArgument(
                    PetoraDestination.PLACE_NAME
                ) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(
                    PetoraDestination.LATITUDE
                ) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(
                    PetoraDestination.LONGITUDE
                ) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val placeName =
                backStackEntry.arguments
                    ?.getString(
                        PetoraDestination.PLACE_NAME
                    )

            val latitude =
                backStackEntry.arguments
                    ?.getString(
                        PetoraDestination.LATITUDE
                    )
                    ?.toDoubleOrNull()

            val longitude =
                backStackEntry.arguments
                    ?.getString(
                        PetoraDestination.LONGITUDE
                    )
                    ?.toDoubleOrNull()

            val placeContext =
                if (
                    placeName != null &&
                    latitude != null &&
                    longitude != null
                ) {
                    PlaceContext(
                        name = placeName,
                        latitude = latitude,
                        longitude = longitude,
                    )
                } else {
                    null
                }

            ChatScreen(
                placeContext = placeContext,
            )
        }

        composable(
            route = PetoraDestination.MAP,
        ) {
            MapScreen(
                onAskMomoClick = { placeContext ->
                    navController.navigate(
                        PetoraDestination.chatWithPlace(
                            placeName = placeContext.name,
                            latitude = placeContext.latitude,
                            longitude = placeContext.longitude,
                        )
                    )
                },
            )
        }
    }
}