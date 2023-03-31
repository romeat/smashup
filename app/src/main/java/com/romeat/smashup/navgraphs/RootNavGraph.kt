package com.romeat.smashup.navgraphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.romeat.smashup.presentation.startup.StartupScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.STARTUP
    ) {

        composable(route = RootGraph.STARTUP) {
            StartupScreen(navController = navController)
        }

        authNavGraph(navController = navController)

        homeNavGraph(navController = navController)

    }
}

object RootGraph {
    const val ROOT = "ROOT_GRAPH"
    const val STARTUP = "STARTUP"
    const val AUTHENTICATION = "AUTH_GRAPH"
    const val HOME = "HOME_GRAPH"
}