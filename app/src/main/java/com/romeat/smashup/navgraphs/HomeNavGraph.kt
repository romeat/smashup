package com.romeat.smashup.navgraphs

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.ContentScreenWithPlayer
import com.romeat.smashup.presentation.home.common.mashup.MashupScreen
import com.romeat.smashup.presentation.home.common.playlist.PlaylistScreen
import com.romeat.smashup.presentation.home.common.source.SourceScreen
import com.romeat.smashup.presentation.home.common.user.UserScreen
import com.romeat.smashup.presentation.home.favourite.CollectionScreen
import com.romeat.smashup.presentation.home.main.MainScreen
import com.romeat.smashup.presentation.home.player.AudioPlayerScreen
import com.romeat.smashup.presentation.home.profile.ProfileScreen
import com.romeat.smashup.presentation.home.search.SearchScreen
import com.romeat.smashup.util.CommonNavigationConstants

// used scoped viewmodel
// (reference: https://stackoverflow.com/questions/64955859/scoping-states-in-jetpack-compose)
@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        startDestination = HomeGraphScreen.Main.route,
        route = RootGraph.HOME
    ) {

        /**
         * Common navigation lambdas
         */
        val onMashupInfoClick: (Int) -> Unit =
            { id -> navController.navigate("${HomeGraphScreen.Mashup.route}/${id}") }

        val onSourceClick: (Int) -> Unit =
            { id -> navController.navigate("${HomeGraphScreen.Source.route}/${id}") }

        val onAuthorClick: (Int) -> Unit =
            { alias -> navController.navigate("${HomeGraphScreen.Author.route}/${alias}") }

        val onPlaylistClick: (Int) -> Unit =
            { id -> navController.navigate("${HomeGraphScreen.Playlist.route}/${id}") }

        val onBackClicked: () -> Unit = { navController.popBackStack() }

        val onExpandPlayerClicked: () -> Unit =
            { navController.navigate(HomeGraphScreen.AudioPlayer.route) }


        /**
         * Bottom bar root destinations
         */
        // HOME
        composable(
            route = HomeGraphScreen.Main.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    MainScreen(
                        onPlaylistClick = onPlaylistClick,
                        onNotificationsClick = { },
                        onSettingsClick = { navController.navigate(HomeGraphScreen.Settings.route) }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        // SEARCH
        composable(
            route = HomeGraphScreen.Search.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    SearchScreen(
                        onUserClick = onAuthorClick,
                        onSourceClick = onSourceClick,
                        onPlaylistClick = onPlaylistClick,
                        onMashupInfoClick = onMashupInfoClick,
                        onBackClick = {
                            navController.navigate(HomeGraphScreen.Main.route) {
                                popUpTo(RootGraph.HOME)
                            }
                        }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        // COLLECTION
        composable(
            route = HomeGraphScreen.Collection.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    CollectionScreen(
                        onPlaylistClick = onPlaylistClick,
                        onMashupInfoClick = onMashupInfoClick,
                        onBackClick = {
                            navController.navigate(HomeGraphScreen.Main.route) {
                                popUpTo(RootGraph.HOME)
                            }
                        }
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        /**
         * Settings sub-graph
         */
        settingsNavGraph(navController = navController)


        /**
         * Inner destinations
         */

        /**
         * Playlist screen
         */
        composable(
            route = "${HomeGraphScreen.Playlist.route}/{${CommonNavigationConstants.PLAYLIST_PARAM}}",
            arguments = listOf(navArgument(CommonNavigationConstants.PLAYLIST_PARAM) {
                type = NavType.IntType
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    PlaylistScreen(
                        onMashupInfoClick = onMashupInfoClick,
                        onAuthorClick = onAuthorClick,
                        onBackClicked = onBackClicked
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        /**
         * Mashup screen
         */
        composable(
            route = "${HomeGraphScreen.Mashup.route}/{${CommonNavigationConstants.MASHUP_PARAM}}",
            arguments = listOf(navArgument(CommonNavigationConstants.MASHUP_PARAM) {
                type = NavType.IntType
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    MashupScreen(
                        onBackClicked = onBackClicked,
                        onAuthorClick = onAuthorClick,
                        onSourceClick = onSourceClick
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        /**
         * Source screen
         */
        composable(
            route = "${HomeGraphScreen.Source.route}/{${CommonNavigationConstants.SOURCE_PARAM}}",
            arguments = listOf(navArgument(CommonNavigationConstants.SOURCE_PARAM) {
                type = NavType.IntType
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    SourceScreen(
                        onBackClicked = onBackClicked,
                        onMashupInfoClick = onMashupInfoClick,
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        /**
         * Author screen
         */
        composable(
            route = "${HomeGraphScreen.Author.route}/{${CommonNavigationConstants.USER_PARAM}}",
            arguments = listOf(navArgument(CommonNavigationConstants.USER_PARAM) {
                type = NavType.IntType
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ContentScreenWithPlayer(
                content = {
                    UserScreen(
                        onBackClick = onBackClicked,
                        onMashupInfoClick = onMashupInfoClick,
                        onPlaylistClick = onPlaylistClick,
                    )
                },
                onExpandPlayerClick = onExpandPlayerClicked,
                playerViewModel = playerViewModel,
                navHostController = navController
            )
        }

        /**
         * Audio player screen
         */
        composable(
            route = HomeGraphScreen.AudioPlayer.route,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(durationMillis = 300),
                    initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(durationMillis = 300),
                    targetOffsetY = { it })
            },
            popExitTransition = {
                slideOutVertically(
                    animationSpec = tween(durationMillis = 300),
                    targetOffsetY = { it })
            },
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val viewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            AudioPlayerScreen(
                onBackPressed = onBackClicked,
                viewModel = viewModel
            )
        }
    }
}

sealed class HomeGraphScreen(val route: String) {

    object AudioPlayer : HomeGraphScreen(route = "AUDIO_PLAYER")

    object Main : HomeGraphScreen(route = "MAIN")
    object Search : HomeGraphScreen(route = "SEARCH")
    object Collection : HomeGraphScreen(route = "COLLECTION")
    object Settings : HomeGraphScreen(route = "SETTINGS")

    object Playlist : HomeGraphScreen(route = CommonNavigationConstants.PLAYLIST_ROUTE)
    object Mashup : HomeGraphScreen(route = CommonNavigationConstants.MASHUP_ROUTE)
    object Source : HomeGraphScreen(route = CommonNavigationConstants.SOURCE_ROUTE)
    object Author : HomeGraphScreen(route = CommonNavigationConstants.USER_ROUTE)
}

sealed class BottomBarScreen(
    val graphScreen: HomeGraphScreen,
    val titleResource: Int,
    val iconRes: Int
) {
    object Main : BottomBarScreen(
        graphScreen = HomeGraphScreen.Main,
        titleResource = R.string.bottom_bar_main,
        iconRes = R.drawable.ic_bottom_bar_home
    )

    object Search : BottomBarScreen(
        graphScreen = HomeGraphScreen.Search,
        titleResource = R.string.bottom_bar_search,
        iconRes = R.drawable.ic_bottom_bar_search
    )

    object Profile : BottomBarScreen(
        graphScreen = HomeGraphScreen.Collection,
        titleResource = R.string.bottom_bar_collection,
        iconRes = R.drawable.ic_bottom_bar_collection
    )
}
