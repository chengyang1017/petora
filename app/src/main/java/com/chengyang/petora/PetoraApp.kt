package com.chengyang.petora

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.chengyang.petora.navigation.PetoraNavHost

@Composable
fun PetoraApp() {
    val navController = rememberNavController()

    PetoraNavHost(
        navController = navController,
    )
}