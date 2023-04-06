package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.romeat.smashup.navgraphs.BottomBarScreen
import com.romeat.smashup.navgraphs.RootGraph

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
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
        selected = navController.backQueue.any {
            it.destination.route == screen.graphScreen.route
        },

        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            if (currentDestination?.route != screen.graphScreen.route) {
                //Log.d("TBAG", "onClick: currentDestination?.route = " + currentDestination?.route + " ___ screen.graphScreen.route = " + screen.graphScreen.route)

                // notOnSameTab means back queue does not contain this tab
                // (== true when we move from one tab to another, == false when we tap on already selected tab)
                val notOnSameTab = navController.backQueue.none {
                    it.destination.route == screen.graphScreen.route
                }

                // so if we not on same tab, we must save backstack and restore
                // state when reselecting a previously selected item
                navController.navigate(screen.graphScreen.route) {
                    popUpTo(RootGraph.HOME) { saveState = notOnSameTab }
                    launchSingleTop = true
                    restoreState = notOnSameTab
                }
            } else {
                // when we are on the same tab on start destination, do nothing
            }
        }
    )
}