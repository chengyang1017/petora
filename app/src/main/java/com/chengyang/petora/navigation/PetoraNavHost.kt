package com.chengyang.petora.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chengyang.petora.feature.chat.ChatScreen
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
                    navController.navigate(PetoraDestination.CHAT)
                },
                onMapClick = {
                    navController.navigate(PetoraDestination.MAP)
                },
            )
        }

        composable(
            route = PetoraDestination.CHAT,
        ) {
            ChatScreen()
        }

        composable(
            route = PetoraDestination.MAP,
        ) {
            MapScreen()
        }
    }
}