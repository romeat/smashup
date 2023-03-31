package com.romeat.smashup.navgraphs

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.HomeScreen
import com.romeat.smashup.presentation.home.player.AudioPlayerScreen

// used scoped viewmodels
// (reference: https://stackoverflow.com/questions/64955859/scoping-states-in-jetpack-compose)
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        startDestination = HomeGraphScreen.Home.route,
        route = RootGraph.HOME
    ) {
        composable(route = HomeGraphScreen.Home.route) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val viewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            HomeScreen(
                rootNavController = navController,
                viewModel = viewModel
            )
        }
        composable(route = HomeGraphScreen.AudioPlayer.route) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val viewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            AudioPlayerScreen(
                onBackPressed = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}

sealed class HomeGraphScreen(val route: String) {
    object Home : HomeGraphScreen(route = "HOME")
    object AudioPlayer : HomeGraphScreen(route = "AUDIO_PLAYER")
}
