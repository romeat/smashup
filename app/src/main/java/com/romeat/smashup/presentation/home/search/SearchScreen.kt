package com.romeat.smashup.presentation.home.search

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.romeat.smashup.navgraphs.BottomBarScreen
import com.romeat.smashup.presentation.home.common.author.AuthorScreen
import com.romeat.smashup.presentation.home.common.mashup.MashupScreen
import com.romeat.smashup.presentation.home.common.playlist.PlaylistScreen
import com.romeat.smashup.presentation.home.common.source.SourceScreen
import com.romeat.smashup.presentation.home.main.MainTabScreen
import com.romeat.smashup.util.CommonNavigationConstants

@Composable
fun SearchScreen(
    navController : NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = BottomBarScreen.Search.route,
        startDestination = SearchTabScreen.SearchBar.route
    ) {
        composable(route = SearchTabScreen.SearchBar.route) {
            SearchBarScreen(
                onAuthorClick = { alias -> navController.navigate("${SearchTabScreen.Author.route}/${alias}") },
                onSourceClick = { id -> navController.navigate("${SearchTabScreen.Source.route}/${id}") },
                onPlaylistClick = { id -> navController.navigate("${MainTabScreen.Playlist.route}/${id}") },
                onMashupInfoClick = { id -> navController.navigate("${SearchTabScreen.Mashup.route}/${id}") }
            )
        }

        composable(
            route = "${SearchTabScreen.Playlist.route}/{${SearchTabScreen.Playlist.paramName}}",
            arguments = listOf(navArgument(SearchTabScreen.Playlist.paramName) {
                type = NavType.IntType
            }),
        ) {
            PlaylistScreen(
                onAuthorClick = { alias -> navController.navigate("${SearchTabScreen.Author.route}/${alias}") },
                onMashupInfoClick = { id -> navController.navigate("${SearchTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${SearchTabScreen.Mashup.route}/{${SearchTabScreen.Mashup.paramName}}",
            arguments = listOf(navArgument(SearchTabScreen.Mashup.paramName) {
                type = NavType.IntType
            }),
        ) {
            MashupScreen(
                onAuthorClick = { alias -> navController.navigate("${SearchTabScreen.Author.route}/${alias}") },
                onSourceClick = { id -> navController.navigate("${SearchTabScreen.Source.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${SearchTabScreen.Source.route}/{${SearchTabScreen.Source.paramName}}",
            arguments = listOf(navArgument(SearchTabScreen.Source.paramName) {
                type = NavType.IntType
            }),
        ) {
            SourceScreen(
                onMashupInfoClick = { id -> navController.navigate("${SearchTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = "${SearchTabScreen.Author.route}/{${SearchTabScreen.Author.paramName}}",
            arguments = listOf(navArgument(SearchTabScreen.Author.paramName) {
                type = NavType.StringType
            }),
        ) {
            AuthorScreen(
                onMashupInfoClick = { id -> navController.navigate("${SearchTabScreen.Mashup.route}/${id}") },
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}

sealed class SearchTabScreen() {
    abstract val route: String

    object SearchBar : SearchTabScreen() {
        override val route: String = "SEARCH_BAR"
    }

    object Playlist : SearchTabScreen() {
        override val route: String = CommonNavigationConstants.PLAYLIST_ROUTE
        const val paramName: String = CommonNavigationConstants.PLAYLIST_PARAM
    }

    object Mashup : SearchTabScreen() {
        override val route: String = CommonNavigationConstants.MASHUP_ROUTE
        val paramName: String = CommonNavigationConstants.MASHUP_PARAM
    }

    object Source : SearchTabScreen() {
        override val route: String = CommonNavigationConstants.SOURCE_ROUTE
        val paramName: String = CommonNavigationConstants.SOURCE_PARAM
    }

    object Author : SearchTabScreen() {
        override val route: String = CommonNavigationConstants.AUTHOR_ROUTE
        val paramName: String = CommonNavigationConstants.AUTHOR_PARAM
    }
}