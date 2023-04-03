package com.romeat.smashup.presentation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.romeat.smashup.navgraphs.BottomBarScreen
import com.romeat.smashup.navgraphs.HomeGraphScreen
import com.romeat.smashup.navgraphs.HomeNavGraph
import com.romeat.smashup.presentation.home.common.composables.PlayerSmall

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    rootNavController: NavController,
    viewModel: HomePlayerViewModel
) {
    Scaffold(
        modifier = Modifier,
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            Row(modifier = Modifier.weight(1.0f)) {
                HomeNavGraph(navController = navController, rootNavController = rootNavController)
            }

            PlayerSmall(
                onPlayClick = { viewModel.onPlayPauseClick() },
                onPreviousClick = { viewModel.onPreviousClick() },
                onNextClick = { viewModel.onNextClick() },
                onExpandClick = { rootNavController.navigate(HomeGraphScreen.AudioPlayer.route) },
                state = viewModel.state,
                currentTimeMs = viewModel.currentTimeMs
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Main,
        BottomBarScreen.Search,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = stringResource(id = screen.titleResource))
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = stringResource(id = screen.titleResource)
            )
        },
        selected =
        currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,

        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            if (currentDestination?.route != screen.route) {
                navController.navigate(screen.route) {
                    popUpTo(screen.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    )
}
