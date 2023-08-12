package com.romeat.smashup.navgraphs

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navigation
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.ContentScreenWithPlayer
import com.romeat.smashup.presentation.home.settings.SettingsScreen
import com.romeat.smashup.presentation.home.settings.about.AboutScreen
import com.romeat.smashup.presentation.home.settings.password.ChangePasswordScreen
import com.romeat.smashup.presentation.home.settings.password.ConfirmPasswordScreen
import com.romeat.smashup.presentation.home.settings.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.settingsNavGraph(navController: NavHostController) {

    navigation(
        startDestination = SettingsGraphScreens.Settings.route,
        route = HomeGraphScreen.Settings.route
    ) {

        val confirmPasswordDeeplink = "https://smashup.ru/user/change_password/confirm?id={id}&userId={userId}"

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
                            navController.navigate(RootGraph.AUTHENTICATION) {
                                popUpTo(RootGraph.ROOT)
                            }
                        }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        // PROFILE
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
                        toEditPassword = { navController.navigate(SettingsGraphScreens.ChangePassword.route) }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        // CHANGE PASSWORD
        composable(
            route = SettingsGraphScreens.ChangePassword.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSuccessClick = { navController.popBackStack() },
            )
        }

        // CONFIRM CHANGE PASSWORD
        composable(
            route = SettingsGraphScreens.ConfirmChangePassword.route,
            deepLinks = listOf(navDeepLink { uriPattern = confirmPasswordDeeplink }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ConfirmPasswordScreen (
                onClose = { navController.popBackStack() },
            )
        }

        // ABOUT
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
    object ChangePassword : SettingsGraphScreens(route = "CHANGE_PASSWORD")
    object ConfirmChangePassword : SettingsGraphScreens(route = "CONFIRM_CHANGE_PASSWORD")
}