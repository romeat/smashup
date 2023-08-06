package com.romeat.smashup.presentation.home.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.composables.BottomNavBar
import com.romeat.smashup.presentation.home.common.composables.PlayerSmall

@Composable
fun ContentScreenWithPlayer(
    content: @Composable () -> Unit,
    onExpandPlayerClick: () -> Unit,
    playerViewModel: HomePlayerViewModel,
    navHostController: NavHostController,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                content()
            }
            PlayerSmall(
                onExpandClick = onExpandPlayerClick,
                viewModel = playerViewModel
            )
            BottomNavBar(navController = navHostController)
        }
    }
}