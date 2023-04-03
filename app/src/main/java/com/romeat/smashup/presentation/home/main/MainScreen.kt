package com.romeat.smashup.presentation.home.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.romeat.smashup.navgraphs.BottomBarScreen
import com.romeat.smashup.presentation.home.common.author.AuthorScreen
import com.romeat.smashup.presentation.home.common.mashup.MashupScreen
import com.romeat.smashup.presentation.home.common.playlist.PlaylistScreen
import com.romeat.smashup.presentation.home.common.source.SourceScreen
import com.romeat.smashup.util.CommonNavigationConstants

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = navController,
        route = BottomBarScreen.Main.route,
        startDestination = MainTabScreen.Charts.route
    ) {

        composable(
            route = MainTabScreen.Charts.route
        ) {
            ChartsScreen(
                onPlaylistClick = { id -> navController.navigate("${MainTabScreen.Playlist.route}/${id}") }
            )
        }

        composable(
            route = "${MainTabScreen.Playlist.route}/{${MainTabScreen.Playlist.paramName}}", // "PLAYLIST/{playlistId}"
            arguments = listOf(navArgument(MainTabScreen.Playlist.paramName) {
                type = NavType.IntType
            }),
        ) {
            PlaylistScreen(
                onAuthorClick = { alias -> navController.navigate("${MainTabScreen.Author.route}/${alias}") },
                onMashupInfoClick = { id -> navController.navigate("${MainTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${MainTabScreen.Mashup.route}/{${MainTabScreen.Mashup.paramName}}",
            arguments = listOf(navArgument(MainTabScreen.Mashup.paramName) {
                type = NavType.IntType
            }),
        ) {
            MashupScreen(
                onAuthorClick = { alias -> navController.navigate("${MainTabScreen.Author.route}/${alias}") },
                onSourceClick = { id -> navController.navigate("${MainTabScreen.Source.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${MainTabScreen.Source.route}/{${MainTabScreen.Source.paramName}}",
            arguments = listOf(navArgument(MainTabScreen.Source.paramName) {
                type = NavType.IntType
            }),
        ) {
            SourceScreen(
                onMashupInfoClick = { id -> navController.navigate("${MainTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${MainTabScreen.Author.route}/{${MainTabScreen.Author.paramName}}",
            arguments = listOf(navArgument(MainTabScreen.Author.paramName) {
                type = NavType.StringType
            }),
        ) {
            AuthorScreen(
                onMashupInfoClick = { id -> navController.navigate("${MainTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}

sealed class MainTabScreen() {
    abstract val route: String

    object Charts : MainTabScreen() {
        override val route: String = "CHARTS"
    }

    object Playlist : MainTabScreen() {
        override val route: String = CommonNavigationConstants.PLAYLIST_ROUTE
        const val paramName: String = CommonNavigationConstants.PLAYLIST_PARAM
    }

    object Mashup : MainTabScreen() {
        override val route: String = CommonNavigationConstants.MASHUP_ROUTE
        val paramName: String = CommonNavigationConstants.MASHUP_PARAM
    }

    object Source : MainTabScreen() {
        override val route: String = CommonNavigationConstants.SOURCE_ROUTE
        val paramName: String = CommonNavigationConstants.SOURCE_PARAM
    }

    object Author : MainTabScreen() {
        override val route: String = CommonNavigationConstants.AUTHOR_ROUTE
        val paramName: String = CommonNavigationConstants.AUTHOR_PARAM
    }

    //object Charts: MainTabScreen(route = "CHARTS")
    //class Playlist{val paramName: String = "playlistId"} : MainTabScreen(route = "PLAYLIST")

    //object Mashup : MainTabScreen(route = "MASHUP")
    // object Source : MainTabScreen(route = "SOURCE")
    //object Author : MainTabScreen(route = "AUTHOR")
}
