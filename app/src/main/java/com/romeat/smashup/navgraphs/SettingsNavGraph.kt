package com.romeat.smashup.navgraphs

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navigation
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.ContentScreenWithPlayer
import com.romeat.smashup.presentation.home.settings.SettingsScreen
import com.romeat.smashup.presentation.home.settings.about.AboutScreen
import com.romeat.smashup.presentation.home.settings.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.settingsNavGraph(navController: NavHostController) {

    navigation(
        startDestination = SettingsGraphScreens.Settings.route,
        route = HomeGraphScreen.Settings.route
    ) {
        val onExpandPlayerClicked: () -> Unit =
            { navController.navigate(HomeGraphScreen.AudioPlayer.route) }

        composable(
            route = SettingsGraphScreens.Settings.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    SettingsScreen(
                        toProfile = { navController.navigate(SettingsGraphScreens.Profile.route) },
                        toAboutApp = { navController.navigate(SettingsGraphScreens.About.route) },
                        onBackClick = { navController.popBackStack() },
                        toAuthScreen = {
                            navController.popBackStack()
                            navController.navigate(RootGraph.AUTHENTICATION)
                        }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        composable(
            route = SettingsGraphScreens.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    ProfileScreen(
                        onBackClick = { navController.popBackStack() },
                        toEditPassword = { /* TODO */ }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        composable(
            route = SettingsGraphScreens.About.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class SettingsGraphScreens(val route: String) {
    object Settings : SettingsGraphScreens(route = "SETTINGS_LIST")
    object Profile : SettingsGraphScreens(route = "PROFILE")
    object About : SettingsGraphScreens(route = "ABOUT")
}