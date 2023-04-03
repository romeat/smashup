package com.romeat.smashup.navgraphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.profile.ProfileScreen
import com.romeat.smashup.presentation.home.main.MainScreen
import com.romeat.smashup.presentation.home.search.SearchScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    rootNavController: NavController
) {
    AnimatedNavHost(
        navController = navController,
        route = RootGraph.HOME,
        startDestination = BottomBarScreen.Main.route
    ) {

        composable(route = BottomBarScreen.Main.route) {
            MainScreen()
        }

        composable(route = BottomBarScreen.Search.route) {
            SearchScreen()
        }

        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(RootGraph.AUTHENTICATION)
                }
            )
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val titleResource: Int,
    val icon: ImageVector
) {
    object Main : BottomBarScreen(
        route = "MAIN",
        titleResource = R.string.bottom_bar_main,
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = "SEARCH",
        titleResource = R.string.bottom_bar_search,
        icon = Icons.Default.Search
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        titleResource = R.string.bottom_bar_profile,
        icon = Icons.Default.Person
    )
}
