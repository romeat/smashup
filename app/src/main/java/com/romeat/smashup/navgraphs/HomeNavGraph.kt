package com.romeat.smashup.navgraphs

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.author.AuthorScreen
import com.romeat.smashup.presentation.home.common.mashup.MashupScreen
import com.romeat.smashup.presentation.home.common.playlist.PlaylistScreen
import com.romeat.smashup.presentation.home.common.source.SourceScreen
import com.romeat.smashup.presentation.home.main.MainScreen
import com.romeat.smashup.presentation.home.player.AudioPlayerScreen
import com.romeat.smashup.presentation.home.profile.ProfileScreen
import com.romeat.smashup.presentation.home.search.SearchBarScreen
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
        val onMashupInfoClick: (Int) -> Unit = { id -> navController.navigate("${HomeGraphScreen.Mashup.route}/${id}") }
        val onSourceClick: (Int) -> Unit = { id -> navController.navigate("${HomeGraphScreen.Source.route}/${id}") }
        val onAuthorClick: (String) -> Unit = { alias -> navController.navigate("${HomeGraphScreen.Author.route}/${alias}") }
        val onPlaylistClick: (Int) -> Unit = { id -> navController.navigate("${HomeGraphScreen.Playlist.route}/${id}") }
        val onBackClicked: () -> Unit = { navController.popBackStack() }


        /**
         * Bottom bar root destinations
         */
        composable(
            route = HomeGraphScreen.Main.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            MainScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onPlaylistClick = onPlaylistClick
            )
        }

        composable(
            route = HomeGraphScreen.Search.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            SearchBarScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onAuthorClick = onAuthorClick,
                onSourceClick = onSourceClick,
                onPlaylistClick = onPlaylistClick,
                onMashupInfoClick = onMashupInfoClick,
            )
        }

        composable(
            route = HomeGraphScreen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            ProfileScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onLogoutClick = {
                    navController.popBackStack()
                    navController.navigate(RootGraph.AUTHENTICATION)
                }
            )
        }


        /**
         * Inner destinations
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
            PlaylistScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onBackClicked = onBackClicked,
                onMashupInfoClick = onMashupInfoClick,
                onAuthorClick = onAuthorClick,
            )
        }

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
            MashupScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onBackClicked = onBackClicked,
                onAuthorClick = onAuthorClick,
                onSourceClick = onSourceClick,
            )
        }

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
            SourceScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onBackClicked = onBackClicked,
                onMashupInfoClick = onMashupInfoClick,
            )
        }

        composable(
            route = "${HomeGraphScreen.Author.route}/{${CommonNavigationConstants.AUTHOR_PARAM}}",
            arguments = listOf(navArgument(CommonNavigationConstants.AUTHOR_PARAM) {
                type = NavType.StringType
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(RootGraph.HOME) }
            val playerViewModel = hiltViewModel<HomePlayerViewModel>(parentEntry)
            AuthorScreen(
                navHostController = navController,
                playerViewModel = playerViewModel,
                onBackClicked = onBackClicked,
                onMashupInfoClick = onMashupInfoClick,
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
    object Profile : HomeGraphScreen(route = "PROFILE")

    object Playlist : HomeGraphScreen(route = CommonNavigationConstants.PLAYLIST_ROUTE)
    object Mashup : HomeGraphScreen(route = CommonNavigationConstants.MASHUP_ROUTE)
    object Source : HomeGraphScreen(route = CommonNavigationConstants.SOURCE_ROUTE)
    object Author : HomeGraphScreen(route = CommonNavigationConstants.AUTHOR_ROUTE)
}

sealed class BottomBarScreen(
    val graphScreen: HomeGraphScreen,
    val titleResource: Int,
    val icon: ImageVector
) {
    object Main : BottomBarScreen(
        graphScreen = HomeGraphScreen.Main,
        titleResource = R.string.bottom_bar_main,
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        graphScreen = HomeGraphScreen.Search,
        titleResource = R.string.bottom_bar_search,
        icon = Icons.Default.Search
    )

    object Profile : BottomBarScreen(
        graphScreen = HomeGraphScreen.Profile,
        titleResource = R.string.bottom_bar_profile,
        icon = Icons.Default.Person
    )
}
