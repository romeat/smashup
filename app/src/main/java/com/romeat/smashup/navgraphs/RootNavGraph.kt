package com.romeat.smashup.navgraphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.romeat.smashup.presentation.startup.StartupScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(navController: NavHostController) {
    AnimatedNavHost(
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